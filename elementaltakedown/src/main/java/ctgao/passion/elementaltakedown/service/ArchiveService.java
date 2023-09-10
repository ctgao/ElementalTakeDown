package ctgao.passion.elementaltakedown.service;

import ctgao.passion.elementaltakedown.domain.CharacterCard;
import ctgao.passion.elementaltakedown.domain.User;
import ctgao.passion.elementaltakedown.domain.UserProfile;
import ctgao.passion.elementaltakedown.repository.CharacterCardRepository;
import ctgao.passion.elementaltakedown.repository.UserProfileRepository;
import ctgao.passion.elementaltakedown.repository.UserRepository;
import ctgao.passion.elementaltakedown.service.dto.ArchiveCard;
import ctgao.passion.elementaltakedown.service.dto.UserProfileDTO;
import ctgao.passion.elementaltakedown.service.mapper.ArchiveMapper;
import ctgao.passion.elementaltakedown.service.mapper.UserProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing user profiles and the cards linked to them
 */
@Service
@Transactional
public class ArchiveService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final CharacterCardRepository characterCardRepository;

    private final UserProfileMapper userProfileMapper;

    private final ArchiveMapper archiveMapper;

    private final CacheManager cacheManager;

    public ArchiveService(
        UserRepository userRepository,
        UserProfileRepository userProfileRepository,
        CharacterCardRepository characterCardRepository,
        UserProfileMapper userProfileMapper,
        ArchiveMapper archiveMapper,
        CacheManager cacheManager
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.characterCardRepository = characterCardRepository;
        this.userProfileMapper = userProfileMapper;
        this.archiveMapper = archiveMapper;
        this.cacheManager = cacheManager;
    }

    public UserProfileDTO findProfileByLogin(String login){
        // get all the user profiles linked to a user
        List<UserProfile> listOfAllProfiles = userProfileRepository.findAllWithEagerRelationships()
            .stream()
            .filter(userProfile -> userProfile.getUser() != null)
            .collect(Collectors.toList());

        // turn them into dtos
        List<UserProfileDTO> listOfProfileDTO = userProfileMapper.profilesToDTOs(listOfAllProfiles)
            .stream()
            .filter(upDTO -> upDTO.getLogin() != null)
            .filter(upDTO -> upDTO.getLogin().equals(login))
            .collect(Collectors.toList());

        // now see if you have a user profile
        if(listOfProfileDTO.isEmpty()){
            // don't have a profile??? we will create you one!
            return createEmptyProfile(login);
        }
        return listOfProfileDTO.get(0);
    }

    private UserProfileDTO createEmptyProfile(String login) {
        Optional<User> userToFind = userRepository.findOneByLogin(login);
        if(userToFind.isPresent()) {
            UserProfile profile = userProfileRepository.save(new UserProfile().user(userToFind.get()));
            return userProfileMapper.profileToDTO(profile);
        }
        // if we get here then that's an invalid login!!!
        return null;
    }

    public List<ArchiveCard> getUserOwnedCards(UserProfileDTO profileDTO){
        // get the cards this person owns
        List<CharacterCard> ownedCards = new ArrayList<>(profileDTO.getCards());

        // change their cards to archive cards
        List<ArchiveCard> ownedArchiveCards = archiveMapper.charactersToArchives(ownedCards);
        ownedArchiveCards.stream().forEach(archiveCard -> archiveCard.setOwned(true));

        return ownedArchiveCards;
    }

    public List<ArchiveCard> getAllCardsWithOwnership(UserProfileDTO profileDTO){
        // get all the cards
        List<CharacterCard> allCards = characterCardRepository.findAll();
        // get all the ids of the cards this profile owns
        List<Long> ownedCardsById = profileDTO.getCards()
            .stream()
            .map(card -> card.getId())
            .collect(Collectors.toList());

        // change all cards to archive cards
        List<ArchiveCard> archiveCards = archiveMapper.charactersToArchives(allCards);
        archiveCards.stream().forEach(archiveCard -> archiveCard.setOwned(ownedCardsById.contains(archiveCard.getId())));

        return archiveCards;
    }

    public List<ArchiveCard> getAllCards(){
        return archiveMapper.charactersToArchives(characterCardRepository.findAll());
    }
}
