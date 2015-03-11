package ru.kpfu.bookstore.controllers;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.kpfu.bookstore.Book;

@Controller
@RequestMapping("/jsr303")
public class Jsr303Controller {

  @RequestMapping(value = "/new_book", method = RequestMethod.GET)
  public String newBook(ModelMap map) {
    map.put("book", new Book());
    return "book_form";
  }

  @RequestMapping(value = "/new_book", method = RequestMethod.POST)
  public String jsr303Handler(
    RedirectAttributes redirectAttributes,
    @Valid Book book,
    BindingResult result,
    ModelMap map
  ) {
    
    if (result.hasErrors()) {
      return "book_form";
    }
    else {
      map.put("book", new Book());
      redirectAttributes.addFlashAttribute("message", "<span style=\"color:green\">Book \""+book.getName()+"\" has been added successfully</span>");
      return "redirect:"+MvcUriComponentsBuilder.fromMappingName("JC#newBook").build();
    }
    
  }
}
