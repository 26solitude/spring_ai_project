package org.example.spring_ai_project.ex2.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SpringAIController {

    private final ChatClient chatClient;

    public SpringAIController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/prompt")
    public String prompt() {
        return chatClient.prompt()
                .user("SpringAI를 사용해 정상적으로 ChatGPT에게서 응답이 오는지 확인")
                .call()
                .content();
    }

    @PostMapping("/prompt")
    public String prompt(@RequestBody ChatRequest request) {
        return chatClient.prompt()
                .user(request.getMessage())
                .call()
                .content();
    }

    public static class ChatRequest {
        @JsonProperty("message")
        private String message;

        public String getMessage() {
            return message;
        }
    }

    record ActorFilms(String actor, List<String> movies) {
    }


    @GetMapping("/actor")
    public String actor() {
        ActorFilms actorFilms = chatClient.prompt("Generate the filmography for a random actor")
                .call()
                .entity(ActorFilms.class);

        return actorFilms.actor + ": " + actorFilms.movies;
    }

    @GetMapping("/actors")
    public String actors() {
        List<ActorFilms> actorFilms = chatClient.prompt()
                .user("Generate the filmography of 5 movies for Tom Hanks and Bill Murray")
                .call()
                .entity(new ParameterizedTypeReference<List<ActorFilms>>() {
                });

        return actorFilms.stream()
                .map(actor -> actor.actor() + ": " + String.join(", ", actor.movies()))
                .collect(Collectors.joining("\n"));
    }

    record FootballTeam(int ranking, String team, String coach, String stats, String opinion, List<String> players) {
    }

    @GetMapping("/football")
    public String football() {
        List<FootballTeam> footballTeams = chatClient.prompt()
                .user("2000년대 이후로 세계 최고의 축구 시즌 팀 5개를 한국어로 설명해주세요. 기준은 팀 성적, 전술적인 혁신, 임팩트 순입니다. 순위, 팀 이름, 감독, 성적, 한줄평, 베스트 일레븐 스쿼드가 포함되어야 합니다.")
                .call()
                .entity(new ParameterizedTypeReference<List<FootballTeam>>() {
                });

        return footballTeams.stream()
                .map(team -> String.format("%d위\n팀: %s\n감독: %s\n성적: %s\n한줄평: %s\n선수단: %s",
                        team.ranking(),
                        team.team(),
                        team.coach(),
                        team.stats(),
                        team.opinion(),
                        String.join(", ", team.players())))
                .collect(Collectors.joining("\n\n"));
    }

    @GetMapping("/stream")
    public void stream() {
        Flux<String> output = chatClient.prompt()
                .user("한국어 농담을 들려줘.")
                .stream()
                .content();

        output.subscribe(
            jokePart -> System.out.println("Recieved part: " + jokePart),
            error -> System.out.println("Error: " + error),
            () -> System.out.println("Streaming Complete")
        );
    }
    
//    @GetMapping("/streamRes")
//    public void streamActorFilms(){
//        var converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<ActorFilms>>() {
//        });
//
//        Flux<String> flux = chatClient.prompt()
//                .user(u -> u.text("""
//                        Generate the filmography for a random actor.
//                        {format}
//                        """)
//                .param("format", converter.getFormat()))
//                .stream()
//                .content();
//
//        String content = flux.collectList().block().stream().collect(Collectors.joining());
//        List<ActorFilms> actorFilms = converter.convert(content);
//
//        actorFilms.forEach(film ->{
//            System.out.println("Actor: " + film.actor());
//            System.out.println("Movies: " + String.join(",", film.movies()));
//        });
//    }

    @GetMapping(value = "/streamRes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamResponse(@RequestParam String prompt) {
        return chatClient.prompt()
                .user(u -> u.text(prompt))
                .stream()
                .content(); // 여기서는 추가 가공 없이 그대로 반환
    }




}
