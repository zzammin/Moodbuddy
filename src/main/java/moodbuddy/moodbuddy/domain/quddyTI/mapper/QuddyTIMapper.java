package moodbuddy.moodbuddy.domain.quddyTI.mapper;

import moodbuddy.moodbuddy.domain.quddyTI.dto.response.QuddyTIResDetailDTO;
import moodbuddy.moodbuddy.domain.quddyTI.entity.QuddyTI;
import org.modelmapper.ModelMapper;

public class QuddyTIMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    public static QuddyTIResDetailDTO toQuddyTIResDetailDTO(QuddyTI quddyTI) {
        return modelMapper.map(quddyTI, QuddyTIResDetailDTO.class);
    }
}
