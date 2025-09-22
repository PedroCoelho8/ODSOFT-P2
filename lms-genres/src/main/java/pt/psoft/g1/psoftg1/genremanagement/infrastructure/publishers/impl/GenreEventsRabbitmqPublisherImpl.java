package pt.psoft.g1.psoftg1.genremanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewAMQP;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewAMQPMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.publishers.GenreEventsPublisher;
import pt.psoft.g1.psoftg1.shared.model.GenreEvents;

@Service
@RequiredArgsConstructor
public class GenreEventsRabbitmqPublisherImpl implements GenreEventsPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange directGenreExchange;
    @Autowired
    private final GenreViewAMQPMapper genreViewAMQPMapper;

    @Override
    public GenreViewAMQP sendGenreCreated(Genre genre) {
        return sendGenreEvent(genre, GenreEvents.GENRE_CREATED);
    }


    private GenreViewAMQP sendGenreEvent(Genre genre, String genreEventType) {

        System.out.println("Send Genre event to AMQP Broker: " + genre.getGenre());

        try {
            GenreViewAMQP genreViewAMQP = genreViewAMQPMapper.toGenreViewAMQP(genre);

            ObjectMapper objectMapper = new ObjectMapper();
            String genreViewAMQPinString = objectMapper.writeValueAsString(genreViewAMQP);

            this.template.convertAndSend(directGenreExchange.getName(), genreEventType, genreViewAMQPinString);

            return genreViewAMQP;
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending genre event: '" + ex.getMessage() + "'");

            return null;
        }
    }

}
