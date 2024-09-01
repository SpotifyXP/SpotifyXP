/*
 * The MIT License
 *
 * Copyright 2021 erhannis.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.spotifyxp.deps.xyz.gianlu.zeroconf;

/**
 * Multistage lock customized for NIO Selectors.<br/>
 * Extended rationale:<br/>
 * See, NIO Selectors take locks on their things when they select(), and don't
 * release them while waiting.  If a select happens before any channels are
 * registered, the select never completes.  If a register first calls wakeup
 * to abort the hung select, there's a small chance the select could loop back
 * around again before the register actually got called.  Adding a plain
 * synchronization around the two calls doesn't fix it, because if the wakeup
 * is inside the sync it won't get called and the select won't return, and if
 * it's outside then the loop-around could still happen and nothing's been
 * fixed.<br/>
 * <br/>
 * So, here's my solution.  Consider the following code.<br/>
 * <pre>
 * private final ABLock selectorLock = new ABLock();
 *
 * ...
 *
 * // Channel registration
 * selectorLock.lockA1();
 * try {
 *   getSelector().wakeup();
 *   selectorLock.lockA2();
 *   try {
 *     channels.put(nic, channel.register(getSelector(), SelectionKey.OP_READ));
 *   } finally {
 *     selectorLock.unlockA2();
 *   }
 * } finally {
 *   selectorLock.unlockA1();
 * }
 *
 * ...
 *
 * // Select
 * Selector selector = getSelector();
 * selectorLock.lockB();
 * try {
 *   selector.select();
 * } finally {
 *   selectorLock.unlockB();
 * }
 * </pre>
 * <br/>
 * The goal is to prevent reentering `select` between a `wakeup` and `register`.
 * To that end:<br/>
 * B blocks A2.<br/>
 * A1 blocks B.<br/>
 * A2 blocks B.<br/>
 * Nothing else blocks anything else.<br/>
 * By e.g. "A2 blocks B", I mean "if a lock exists on A2, attempts to acquire a
 * lock on B will block until there are no more locks on A2."  So, in the
 * registration thread, you acquire A1 to prevent any new B locks, call wakeup
 * to get the Selector to drop its B lock, then lock on A2 to wait for it to do
 * so, then make your registration, then drop the locks (in reverse order, as is
 * custom).  On the select thread, you acquire B, then select, then release B.<br/>
 * Unless I've made a goof:<br/>
 * You can have multiple registrations occur at once, and multiple selections
 * occur at once, but never both registrations and selections, and a
 * (lock for a) registration will not wait forever for a selection to conclude.<br/>
 * HOWEVER.<br/>
 * I've only tested this briefly.  I'd want to test it a lot more before I was
 * very comfortable with it, and I don't really have time right now.  I suspect
 * it's better than nothing, at least, though.
 *
 * @author erhannis
 */
public final class ABLock {
    private final Object sync = new Object();
    private long a1 = 0;
    private long a2 = 0;
    private long b = 0;

    /**
     * For every call to lockA1, there must be exactly one subsequent call to unlockA1.<br/>
     * Attempted locks on A1 are not blocked by anything.
     * An existing lock on A1 blocks new locks on B.
     * An existing lock on A1 DOES NOT block new locks on A1 or A2.
     */
    public void lockA1() {
        synchronized (sync) {
            a1++;
            sync.notifyAll();
        }
    }

    public void unlockA1() {
        synchronized (sync) {
            a1--;
            sync.notifyAll();
        }
    }

    /**
     * For every call to lockA2, there must be a subsequent call to unlockA2.<br/>
     * Attempted locks on A2 are blocked by existing locks on B, and nothing else.
     * An existing lock on A2 blocks new locks on B.
     * An existing lock on A2 DOES NOT block new locks on A1 or A2.
     */
    public void lockA2() throws InterruptedException {
        synchronized (sync) {
            while (b > 0)
                sync.wait();

            a2++;
            sync.notifyAll();
        }
    }

    public void unlockA2() {
        synchronized (sync) {
            a2--;
            sync.notifyAll();
        }
    }

    /**
     * For every call to lockB, there must be a subsequent call to unlockB.<br/>
     * Attempted locks on B are blocked by existing locks on A1 and A2, and nothing else.
     * An existing lock on B blocks new locks on A2.
     * An existing lock on B DOES NOT block new locks on A1 or B.
     */
    public void lockB() throws InterruptedException {
        synchronized (sync) {
            while (a1 > 0 || a2 > 0)
                sync.wait();

            b++;
            sync.notifyAll();
        }
    }

    public void unlockB() {
        synchronized (sync) {
            b--;
            sync.notifyAll();
        }
    }
}
