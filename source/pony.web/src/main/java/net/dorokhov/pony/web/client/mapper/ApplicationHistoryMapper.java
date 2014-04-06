package net.dorokhov.pony.web.client.mapper;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import net.dorokhov.pony.web.client.place.ArtistsPlace;

@WithTokenizers({ArtistsPlace.Tokenizer.class})
public interface ApplicationHistoryMapper extends PlaceHistoryMapper {

}
