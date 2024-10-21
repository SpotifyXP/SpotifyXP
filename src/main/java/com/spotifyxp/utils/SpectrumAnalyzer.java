package com.spotifyxp.utils;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class SpectrumAnalyzer {
    private final FastFourierTransformer fft;

    public SpectrumAnalyzer() {
        fft = new FastFourierTransformer(DftNormalization.STANDARD);
    }

    public double[] analyzeAudio(byte[] audioData) {
        try {
            // Convert the byte array to double array (assuming 16-bit PCM data)
            double[] samples = new double[audioData.length / 2];
            for (int i = 0; i < samples.length; i++) {
                int sample = (audioData[i * 2] & 0xFF) | (audioData[i * 2 + 1] << 8);
                samples[i] = sample / 32768.0; // Normalize to the range [-1, 1]
            }

            // Apply windowing function (e.g., Hamming window)
            // You can experiment with other windowing functions like Hanning, Blackman, etc.
            for (int i = 0; i < samples.length; i++) {
                samples[i] *= 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (samples.length - 1));
            }

            // Perform FFT to obtain the spectrum data
            Complex[] spectrum = fft.transform(samples, TransformType.FORWARD);
            double[] spectrumData = new double[spectrum.length / 2]; // We only need half of the data (positive frequencies)

            // Calculate the magnitude of each frequency bin
            for (int i = 0; i < spectrumData.length; i++) {
                double real = spectrum[i].getReal();
                double imag = spectrum[i].getImaginary();
                spectrumData[i] = Math.sqrt(real * real + imag * imag);
            }

            return spectrumData;
        } catch (NullPointerException e) {
            return null;
        }
    }
}