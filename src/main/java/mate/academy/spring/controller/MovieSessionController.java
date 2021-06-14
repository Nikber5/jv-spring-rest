package mate.academy.spring.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import mate.academy.spring.model.MovieSession;
import mate.academy.spring.model.dto.MovieSessionRequestDto;
import mate.academy.spring.model.dto.MovieSessionResponseDto;
import mate.academy.spring.service.MovieSessionService;
import mate.academy.spring.service.mapper.MovieSessionMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movie-sessions")
public class MovieSessionController {
    private final MovieSessionService movieSessionService;
    private final MovieSessionMapper mapper;

    public MovieSessionController(MovieSessionService movieSessionService,
                                  MovieSessionMapper mapper) {
        this.movieSessionService = movieSessionService;
        this.mapper = mapper;
    }

    @GetMapping("/available")
    public List<MovieSessionResponseDto> getAvailableMovieSessions(@RequestParam Long id,
                              @RequestParam
                              @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate date) {
        return movieSessionService.findAvailableSessions(id, date).stream()
                .map(mapper::parse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public MovieSessionResponseDto add(@RequestBody MovieSessionRequestDto requestDto) {
        return mapper.parse(movieSessionService.add(mapper.toModel(requestDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieSessionService.delete(id);

    }

    @PutMapping("/{id}")
    public MovieSessionResponseDto update(@PathVariable Long id,
                                          @RequestBody MovieSessionRequestDto requestDto) {
        MovieSession movieSession = mapper.toModel(requestDto);
        movieSession.setId(id);
        MovieSession updatedSession = movieSessionService.update(movieSession);
        return mapper.parse(updatedSession);
    }
}
