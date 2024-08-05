package org.example.spring1.ticket;

import lombok.RequiredArgsConstructor;
import org.example.spring1.ticket.model.TicketDTO;
import org.example.spring1.ticket.model.TicketRequestDTO;
import org.example.spring1.ticket.model.TicketSearchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.spring1.UrlMapping.*;

@RestController
@RequestMapping(TICKETS)
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    public List<TicketDTO> findAll() {
        return ticketService.findAll();
    }

    @GetMapping(ID_PART)
    public ResponseEntity<TicketDTO> get(@PathVariable Long id) {
        return ticketService.get(id);
    }

    @PostMapping
    public TicketDTO create(@RequestBody TicketRequestDTO dto) {
        return ticketService.create(dto);
    }

    @DeleteMapping(ID_PART)
    public void delete(@PathVariable Long id) {
        ticketService.delete(id);
    }


    @PostMapping(FILTERED_PART)
    public List<TicketDTO> findAllFiltered(@RequestBody TicketSearchDTO searchDTO) {
        return ticketService.findAllFiltered(searchDTO);
    }
}
