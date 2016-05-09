package ru.kpfu.bookstore.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.kpfu.bookstore.exceptions.NotFoundException;
import ru.kpfu.bookstore.models.Book;
import ru.kpfu.bookstore.repositories.BookRepository;
import ru.kpfu.bookstore.repositories.PublishingHouseRepository;

@Controller
public class BookController {

  @Autowired
  private BookRepository bookRepo;

  @Autowired
  private PublishingHouseRepository publishingHouseRepository;

  @RequestMapping("/")
  public String list(ModelMap map) {
    map.put("books", bookRepo.findAll());
    return "books/list";
  }

  @RequestMapping("/book/{id}")
  public String show(@PathVariable("id") Book book, ModelMap map) {
    if (book == null) {
      throw new NotFoundException("book");
    }
    map.put("book", book);
    return "books/show";
  }

  @RequestMapping(value = "/book/new", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ADMIN')")
  public String add(ModelMap map) {
    map.put("book", new Book());
    return showForm(map);
  }

  @RequestMapping(value = "/book/new", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ADMIN')")
  public String addHandler(
    RedirectAttributes redirectAttributes,
    @ModelAttribute("book") @Valid Book book,
    BindingResult result,
    ModelMap map
  ) {
    if (result.hasErrors()) {
      return showForm(map);
    } else {
      bookRepo.save(book);
      redirectAttributes.addFlashAttribute("message", "Book \"" + book.getName() + "\" has been saved successfully");
      redirectAttributes.addFlashAttribute("messageType", "success");
      return "redirect:" + MvcUriComponentsBuilder.fromMappingName("BC#add").build();
    }
  }

  @RequestMapping(value = "/book/edit/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ADMIN')")
  public String edit(@PathVariable("id") Book book, ModelMap map) {
    if (book == null) {
      throw new NotFoundException("book");
    }
    map.put("book", book);
    return showForm(map);
  }

  @RequestMapping(value = "/book/edit/{id}", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ADMIN')")
  public String editHandler(
    RedirectAttributes redirectAttributes,
    @ModelAttribute("book") @Valid Book book,
    BindingResult result,
    ModelMap map
  ) {
    if (result.hasErrors()) {
      return showForm(map);
    } else {
      bookRepo.save(book);
      redirectAttributes.addFlashAttribute("message", "Book \"" + book.getName() + "\" has been saved successfully");
      redirectAttributes.addFlashAttribute("messageType", "success");
      return "redirect:" + MvcUriComponentsBuilder.fromMappingName("BC#edit").arg(0, book).build();
    }
  }

  @RequestMapping("/book/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String delete(@PathVariable("id") Book book, RedirectAttributes redirectAttributes, ModelMap map) {
    try {
      bookRepo.delete(book);
      redirectAttributes.addFlashAttribute("message", "Book has been deleted successfully");
      redirectAttributes.addFlashAttribute("messageType", "success");
    }catch (DataAccessException e) {
      redirectAttributes.addFlashAttribute("message", "Can't delete book.");
      redirectAttributes.addFlashAttribute("messageType", "fail");
    }
    return "redirect:" + MvcUriComponentsBuilder.fromMappingName("BC#list").build();
  }

  protected String showForm(ModelMap map) {
    map.put("publishingHouses", publishingHouseRepository.findAll());
    return "books/book_form";
  }
}
