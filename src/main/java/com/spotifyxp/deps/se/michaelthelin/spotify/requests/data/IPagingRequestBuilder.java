package com.spotifyxp.deps.se.michaelthelin.spotify.requests.data;

import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Paging;
import com.spotifyxp.deps.se.michaelthelin.spotify.requests.IRequest;

public interface IPagingRequestBuilder<T, BT extends IRequest.Builder<Paging<T>, ?>>
  extends IRequest.Builder<Paging<T>, BT> {
  BT limit(final Integer limit);

  BT offset(final Integer offset);
}
