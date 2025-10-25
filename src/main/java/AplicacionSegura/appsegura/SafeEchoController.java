package AplicacionSegura.appsegura;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SafeEchoController {

    @GetMapping("/echo")
    public String echoForm() { return "echo"; }

    @PostMapping("/echo")
    public String echo(@Valid LoginDto dto, Model model) {
        model.addAttribute("username", dto.getUsername());
        return "echo";
    }
}
