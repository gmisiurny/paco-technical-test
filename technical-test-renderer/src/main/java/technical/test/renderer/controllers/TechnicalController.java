package technical.test.renderer.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import technical.test.common.request.PostFlightRequest;
import technical.test.renderer.facades.FlightFacade;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class TechnicalController {

    private static final String EMPTY_STR = "";
    private static final String CREATE_FLIGHT_PAGE = "pages/create-flight";
    private static final String ATTR_POST_FLIGHT_REQUEST = "postFlightRequest";
    private final FlightFacade flightFacade;

    @GetMapping
    public Mono<String> getMarketPlaceReturnCouponPage(final Model model) {
        model.addAttribute("flights", this.flightFacade.getFlights());
        return Mono.just("pages/index");
    }

    @GetMapping("/create-flight")
    public Mono<String> showCreateFlightPage(final Model model) {
        final PostFlightRequest emptyRequest = new PostFlightRequest(
            null, null, 0.0, EMPTY_STR, EMPTY_STR, EMPTY_STR
        );
        model.addAttribute(ATTR_POST_FLIGHT_REQUEST, emptyRequest);
        return Mono.just(CREATE_FLIGHT_PAGE);
    }

    @PostMapping("/create-flight")
    public Mono<String> createFlight(
        @Valid @ModelAttribute final PostFlightRequest postFlightRequest,
        final BindingResult bindingResult,
        final Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(ATTR_POST_FLIGHT_REQUEST, postFlightRequest);
            return Mono.just(CREATE_FLIGHT_PAGE);
        }

        return this.flightFacade.createFlight(postFlightRequest)
            .doOnSuccess(flight -> log.info("Vol créé avec succès: {}", flight))
            .doOnError(error -> log.error("Erreur lors de la création du vol", error))
            .flatMap(flight -> Mono.just("redirect:/"))
            .onErrorResume(error -> {
                model.addAttribute(ATTR_POST_FLIGHT_REQUEST, postFlightRequest);
                return Mono.just(CREATE_FLIGHT_PAGE);
            });
    }

}
