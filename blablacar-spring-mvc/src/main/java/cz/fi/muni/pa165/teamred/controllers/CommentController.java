package cz.fi.muni.pa165.teamred.controllers;

import cz.fi.muni.pa165.teamred.config.UserSession;
import cz.fi.muni.pa165.teamred.dto.CommentCreateDTO;
import cz.fi.muni.pa165.teamred.facade.CommentFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * Created by jcibik on 12/8/17.
 */
@Controller
@RequestMapping("/comment")
public class CommentController {


    final static Logger log = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private UserSession userSession;

    @Autowired
    private CommentFacade commentFacade;

    //only for post method allowed creating a new comment
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createNewComment(@Valid @ModelAttribute("commentCreateDTO") CommentCreateDTO comment,
                                       BindingResult result,
                                       Model model,
                                   RedirectAttributes redirectAttributes,
                                       HttpServletRequest request,
                                       HttpServletResponse response){

        log.debug("create(formBean={})", comment);

        if (result.hasErrors()) {
            for (ObjectError ge : result.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : result.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }
            model.addAttribute("commentCreateDTO", comment);
            return "comments/new";
        }
        //create
        Long id = commentFacade.createComment(comment);
        //report success
        redirectAttributes.addFlashAttribute("alert_success", "Comment " + id + " was created");
        //redirect to ride with this comment
        return "redirect:../ride/showRide/" + comment.getRideId();
    }


    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String addCommentForm(@RequestParam(value = "rideId", required = true) Long rideId, ModelMap model){
        CommentCreateDTO newComment = new CommentCreateDTO();
        newComment.setRideId(rideId);
        newComment.setAuthorId(userSession.getUserId());
        model.addAttribute("commentCreateDTO", newComment);
        //redirect to comment create form
        return "comments/new";
    }

    @RequestMapping("")
    public String redirectTo404Page(Model model, HttpServletRequest request, HttpServletResponse response){
        return "error404";
    }

    @ModelAttribute(name = "userSession")
    public UserSession addUserSession(){
        return userSession;
    }
}
