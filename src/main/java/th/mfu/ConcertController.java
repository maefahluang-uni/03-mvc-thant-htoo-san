package th.mfu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConcertController {

    private Map<Integer, Concert> concerts = new HashMap<>();
    private int nextId = 1;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/concerts")
    public String listConcerts(Model model) {
        model.addAttribute("concerts", concerts.values());
        return "list-concert";
    }

    @GetMapping("/add-concert")
    public String addConcertForm(Model model) {
        model.addAttribute("concert", new Concert());
        return "add-concert-form";
    }

    @PostMapping("/concerts")
    public String saveConcert(@ModelAttribute Concert concert, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("Binding errors: " + result.getAllErrors());
            return "add-concert-form";
        }
        concert.setId(nextId++);
        concerts.put(concert.getId(), concert);
        return "redirect:/concerts";
    }

    @GetMapping("/delete-concert/{id}")
    public String deleteConcert(@PathVariable int id) {
        concerts.remove(id);
        return "redirect:/concerts";
    }

    @GetMapping("/delete-concert")
    public String deleteAllConcerts() {
        concerts.clear();
        nextId = 1;
        return "redirect:/concerts";
    }
}
