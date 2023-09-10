package ctgao.passion.elementaltakedown.service.mapper;

import ctgao.passion.elementaltakedown.domain.UserProfile;
import ctgao.passion.elementaltakedown.service.dto.UserProfileDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link UserProfile} to {@link UserProfileDTO}}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserProfileMapper {
    public List<UserProfileDTO> profilesToDTOs(List<UserProfile> userProfiles) {
        return userProfiles.stream().filter(Objects::nonNull).map(this::profileToDTO).collect(Collectors.toList());
    }

    public UserProfileDTO profileToDTO(UserProfile userProfile) {
        return new UserProfileDTO(userProfile);
    }
}
