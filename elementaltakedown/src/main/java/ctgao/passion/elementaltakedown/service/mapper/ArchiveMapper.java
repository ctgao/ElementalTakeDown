package ctgao.passion.elementaltakedown.service.mapper;

import ctgao.passion.elementaltakedown.domain.CharacterCard;
import ctgao.passion.elementaltakedown.service.dto.ArchiveCard;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link CharacterCard} to {@link ArchiveCard}}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class ArchiveMapper {

    public List<ArchiveCard> charactersToArchives(List<CharacterCard> characterCards) {
        return characterCards.stream().filter(Objects::nonNull).map(this::charaToArchive).collect(Collectors.toList());
    }

    public ArchiveCard charaToArchive(CharacterCard characterCard) {
        return new ArchiveCard(characterCard);
    }

    public List<CharacterCard> archivesToCharacters(List<ArchiveCard> archiveCards) {
        return archiveCards.stream().filter(Objects::nonNull).map(this::archiveToChara).collect(Collectors.toList());
    }

    public CharacterCard archiveToChara(ArchiveCard archiveCard) {
        CharacterCard newCard = new CharacterCard();
        newCard.setId(archiveCard.getId());
        newCard.setName(archiveCard.getName());
        newCard.setElement(archiveCard.getElement());
        newCard.setBasic(archiveCard.getBasic());
        newCard.setSkill(archiveCard.getSkill());
        newCard.setUltimate(archiveCard.getUltimate());
        return newCard;
    }
}
