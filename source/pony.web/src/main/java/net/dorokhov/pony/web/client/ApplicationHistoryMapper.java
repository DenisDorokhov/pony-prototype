package net.dorokhov.pony.web.client;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import net.dorokhov.pony.web.client.place.AlbumListPlace;
import net.dorokhov.pony.web.client.place.ArtistListPlace;

@WithTokenizers({ArtistListPlace.Tokenizer.class, AlbumListPlace.Tokenizer.class})
public interface ApplicationHistoryMapper extends PlaceHistoryMapper {

}
