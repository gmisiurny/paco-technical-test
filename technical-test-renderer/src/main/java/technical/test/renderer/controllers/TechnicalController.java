package technical.test.renderer.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import technical.test.common.request.PostFlightRequest;
import technical.test.renderer.facades.FlightFacade;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class TechnicalController {

    private static final String EMPTY_STR = "";
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_PAGE_SIZE = "5";
    private static final String DEFAULT_SORT_BY = "price";
    private static final String DEFAULT_SORT_DIR = "ASC";
    private static final String CREATE_FLIGHT_PAGE = "pages/create-flight";
    private static final String CREATE_FLIGHT_PATH = "/create-flight";
    private static final String FLIGHT_DETAILS_PATH = "/flight-details";
    private static final String ATTR_POST_FLIGHT_REQUEST = "postFlightRequest";
    private final FlightFacade flightFacade;

    @GetMapping
    public Mono<String> getMarketPlaceReturnCouponPage(
        final Model model,
        @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
        @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
        @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir) {

        final Sort.Direction direction = sortDir.equalsIgnoreCase(DEFAULT_SORT_DIR)
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;

        final Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return this.flightFacade.getFlights(pageable)
            .doOnNext(flightsPage -> {
                model.addAttribute("flights", flightsPage);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", flightsPage.getTotalPages());
                model.addAttribute("totalElements", flightsPage.getTotalElements());
                model.addAttribute("size", size);
                model.addAttribute("sortBy", sortBy);
                model.addAttribute("sortDir", sortDir);
                model.addAttribute("isFirst", flightsPage.isFirst());
                model.addAttribute("isLast", flightsPage.isLast());
            })
            .then(Mono.just("pages/index"));
    }

    @GetMapping(FLIGHT_DETAILS_PATH + "/{id}")
    public Mono<String> selectFlight(
        @PathVariable String id,
        @RequestParam(defaultValue = DEFAULT_PAGE) int page,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
        @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
        @RequestParam(defaultValue = DEFAULT_SORT_DIR) String sortDir,
        final Model model) {

        return this.flightFacade.getFlightById(id)
            .doOnNext(flight -> {
                model.addAttribute("selectedFlight", flight);
                model.addAttribute("returnPage", page);
                model.addAttribute("returnSize", size);
                model.addAttribute("returnSortBy", sortBy);
                model.addAttribute("returnSortDir", sortDir);
                log.info("Vol sélectionné: {}", flight.getId());
            })
            .then(Mono.just("pages/flight-details"));
    }

    @GetMapping(CREATE_FLIGHT_PATH)
    public Mono<String> showCreateFlightPage(final Model model) {
        final PostFlightRequest emptyRequest = new PostFlightRequest(
            null, null, 0.0, EMPTY_STR, EMPTY_STR, EMPTY_STR
        );
        model.addAttribute(ATTR_POST_FLIGHT_REQUEST, emptyRequest);
        return Mono.just(CREATE_FLIGHT_PAGE);
    }

    @PostMapping(CREATE_FLIGHT_PATH)
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
