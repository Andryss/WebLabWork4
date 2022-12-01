package web.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.entities.Request;
import web.entities.RequestDTO;
import web.security.PersonDetails;
import web.services.HistoryService;
import web.util.RequestErrorResponse;
import web.util.RequestFormatException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final HistoryService historyService;

    private final ModelMapper modelMapper;

    @Autowired
    public HistoryController(HistoryService historyService, @Qualifier("modelMapper") ModelMapper modelMapper) {
        this.historyService = historyService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<RequestDTO> getHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return historyService.getPersonHistory(personDetails.getUsername())
                .stream().map((request -> modelMapper.map(request, RequestDTO.class))).collect(Collectors.toList());
    }

    @PostMapping("/new")
    public List<RequestDTO> addRequest(@RequestBody @Valid RequestDTO requestDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            throw new RequestFormatException(bindingResult);

        Request request = modelMapper.map(requestDTO, Request.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        return historyService.addPersonRequest(personDetails.getUsername(), request)
                .stream().map(r -> modelMapper.map(r, RequestDTO.class)).collect(Collectors.toList());
    }

    @ExceptionHandler
    public ResponseEntity<RequestErrorResponse> handleRequestError(RequestFormatException e) {
        return new ResponseEntity<>(
                new RequestErrorResponse(e),
                HttpStatus.BAD_REQUEST
        );
    }
}
