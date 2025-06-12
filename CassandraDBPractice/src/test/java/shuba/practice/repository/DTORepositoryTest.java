package shuba.practice.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import shuba.practice.config.Config;
import shuba.practice.dto.ValidatableDTO;
import shuba.practice.setters.Setter;
import shuba.practice.validation.ValidateDTO;

import java.util.Collections;

import static org.mockito.Mockito.mock;

class DTORepositoryTest {

    PreparedStatement preparedStatement;

    BoundStatement boundStatement;

    CqlSession session;

    Config config;

    ValidatableDTO dto;

    Setter setter;

    DTORepository dtoRepository;


    @BeforeEach
    void setUp() {
        preparedStatement = mock(PreparedStatement.class);
        boundStatement = mock(BoundStatement.class);
        session = mock(CqlSession.class);
        config = mock(Config.class);
        dto = mock(ValidatableDTO.class);
        setter = mock(Setter.class);

        dtoRepository = new DTORepository(config, session);
    }

    @Test
    void insertAllHappyCase() {
        int expectedCount = 5;

        // Настройка моков
        Mockito.when(setter.getColumns()).thenReturn(new String[]{"id", "name"});
        Mockito.when(setter.getTableName()).thenReturn("Category");
        Mockito.when(setter.getCount()).thenReturn(expectedCount);
        Mockito.when(setter.getRandomDTO()).thenReturn(dto);
        Mockito.when(setter.getBoundStatement(preparedStatement, dto)).thenReturn(boundStatement);

        Mockito.when(session.prepare(Mockito.anyString())).thenReturn(preparedStatement);

        Mockito.when(config.getLogNumber()).thenReturn(expectedCount);

        // Мок static ValidateDTO
        try (MockedStatic<ValidateDTO> validateDTOMock = Mockito.mockStatic(ValidateDTO.class)) {
            validateDTOMock.when(() -> ValidateDTO.validateDTO(dto)).thenReturn(Collections.emptyMap());


            dtoRepository.insertAll(setter);

            Mockito.verify(session, Mockito.times(1)).prepare(Mockito.anyString());
            Mockito.verify(setter, Mockito.times(expectedCount)).getRandomDTO();
            Mockito.verify(session, Mockito.times(expectedCount)).execute(boundStatement);
        }
    }

}