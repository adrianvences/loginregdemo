package adrian.loginregdemo.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import adrian.loginregdemo.models.LoginUser;
import adrian.loginregdemo.models.User;
import adrian.loginregdemo.services.UserService;

// .. don't forget to inlcude all your imports! ..

@Controller
public class MainController {

  // Add once service is implemented:
  @Autowired
  private UserService userServ;

  @GetMapping("/")
  public String index(Model model) {

    // Bind empty User and LoginUser objects to the JSP
    // to capture the form input
    model.addAttribute("newUser", new User());
    model.addAttribute("newLogin", new LoginUser());
    return "index.jsp";
  }

  @PostMapping("/register")
  public String register(
    @Valid 
    @ModelAttribute("newUser") User newUser,
    BindingResult result, 
    Model model,
    HttpSession session) {

    User user = userServ.register(newUser, result);
    // TO-DO Later -- call a register method in the service
    // to do some extra validations and create a new user!

    if (result.hasErrors()) {
      // Be sure to send in the empty LoginUser before
      // re-rendering the page.
      model.addAttribute("newLogin", new LoginUser());
      return "index.jsp";
    }

    // No errors!
    // TO-DO Later: Store their ID from the DB in session,
    session.setAttribute("userId", user.getId());
    // in other words, log them in.

    return "redirect:/welcome";
  }

  @PostMapping("/login")
  public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin,
      BindingResult result, Model model, HttpSession session) {

    // Add once service is implemented:
    User user = userServ.login(newLogin, result);

    if (result.hasErrors()) {
      model.addAttribute("newUser", new User());
      return "index.jsp";
    }

    // No errors!
    // TO-DO Later: Store their ID from the DB in session,
    // in other words, log them in.
    // to log in user we use the session to log in

    session.setAttribute("userId", user.getId());

    return "redirect:/welcome";

  }

  @GetMapping("/welcome")
  public String welcome(HttpSession session) {
    // if (!session.getId()){
    //   return "redirect:/"
    // }
    // return "welcome.jsp";

    if (session.isNew() || session.getAttribute("userId") == null) {
      return "redirect:/";
  } else {
      User loggedInUser = userServ.login((LoginUser, BindingResult) session.getAttribute("userId"));
      model.addAttribute("loggedInUser", loggedInUser);
      System.out.println("************** HOME *********** ");
      return "redirect:/welcome";
  }
  }

  // clearing session

  @GetMapping("/logout")
  public String logout(HttpSession session){
    //session.setAttribute("userId",null);
    session.invalidate();
    return "redirect:/";
  }
    
}
