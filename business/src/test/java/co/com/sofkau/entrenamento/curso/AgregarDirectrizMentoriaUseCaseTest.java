package co.com.sofkau.entrenamento.curso;

import co.com.sofka.business.generic.UseCaseHandler;
import co.com.sofka.business.repository.DomainEventRepository;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkau.entrenamiento.curso.commands.AgregarDirectrizMentoria;
import co.com.sofkau.entrenamiento.curso.commands.AgregarMentoria;
import co.com.sofkau.entrenamiento.curso.events.DirectrizAgregadaAMentoria;
import co.com.sofkau.entrenamiento.curso.events.MentoriaCreada;
import co.com.sofkau.entrenamiento.curso.values.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgregarDirectrizMentoriaUseCaseTest {

    @InjectMocks
    private AgregarDirectrizMentoriaUseCase useCase;

    @Mock
    private DomainEventRepository repository;

    @Test
    void agregarDirectrizUnaMentoriaHappyPass(){
        //arrange
        CursoId coursoId = CursoId.of("ddddd");
        MentoriaId mentoriaId = new MentoriaId("xxxx");
        Directiz directiz = new Directiz("Curso DDD");
        var command = new AgregarDirectrizMentoria(coursoId, mentoriaId, directiz);

        when(repository.getEventsBy("xxxx")).thenReturn(storedEvents());
        useCase.addRepository(repository);

        //act

        var events = UseCaseHandler.getInstance()
                .setIdentifyExecutor(command.getMentoriaId().value())
                .syncExecutor(useCase, new RequestCommand<>(command))
                .orElseThrow()
                .getDomainEvents();

        //assert
        var event = (DirectrizAgregadaAMentoria)events.get(0);
        Assertions.assertEquals("Curso DDD", event.getDirectiz().value());
    }

    private List<DomainEvent> storedEvents() {
        MentoriaId mentoriaId = new MentoriaId("xxxx");
        Nombre nombre = new Nombre("Aprendiendo de casos de usos");
        Fecha fecha = new Fecha(LocalDateTime.now(), LocalDate.now());

        var event = new MentoriaCreada(
                mentoriaId,
                nombre,
                fecha
        );

        return List.of(event);
    }
}