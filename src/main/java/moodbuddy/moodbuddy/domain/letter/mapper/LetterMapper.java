package moodbuddy.moodbuddy.domain.letter.mapper;

import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResDetailsDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResSaveDTO;
import moodbuddy.moodbuddy.domain.letter.entity.Letter;
import moodbuddy.moodbuddy.domain.user.entity.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;


public class LetterMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.addMappings(new PropertyMap<Letter, LetterResSaveDTO>() {
            @Override
            protected void configure() {
                map().setLetterId(source.getId());
                map().setLetterDate(source.getLetterDate());
                using(userToUserEmailConverter).map(source.getUser(), destination.getUserEmail());
            }
        });
    }

    private static final Converter<User, Long> userToUserEmailConverter = context -> context.getSource().getUserId();

    public static Letter toLetterEntity(LetterReqDTO letterReqDTO, User user) {
        return Letter.builder()
                .user(user)
                .letterFormat(letterReqDTO.getLetterFormat())
                .letterWorryContent(letterReqDTO.getLetterWorryContent())
                .letterDate(letterReqDTO.getLetterDate())
                .build();
    }

    public static LetterResSaveDTO toLetterSaveDTO(Letter letter) {
        return modelMapper.map(letter, LetterResSaveDTO.class);
    }

    public static LetterResDetailsDTO toLetterDetailsDTO(Letter letter){
        return modelMapper.map(letter, LetterResDetailsDTO.class);
    }
}

