package com.easysoft.ecommerce.controller.admin;

import com.easysoft.ecommerce.dao.UserDao;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.service.ServiceLocator;
import com.easysoft.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    private UserService service;
    private UserDao dao;
    private ServiceLocator serviceLocator;


    @Autowired
    public UserAdminController(UserService service, UserDao dao) {
        this.service = service;
        this.dao = dao;
    }

    @RequestMapping(value = "index.html", method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        return new ModelAndView("admin/users/index");
    }

    @RequestMapping(value = "form.html", method = RequestMethod.GET)
    public ModelAndView displayForm() throws Exception {
        User entity = new User();

        return new ModelAndView("admin/users/form", "command", entity);
    }

//    @RequestMapping(value = "index.html", method = RequestMethod.PUT)
//    public ModelAndView add(@Valid User entity) throws Exception {
//        Site site = serviceLocator.getSystemContext().getSite();
//        entity.setCreatedDate(new Date());
//        entity.setSiteAdmin("Y");
//        entity.setSiteUser("N");
//        entity.setSite(site);
//        service.createOrUpdate(entity);
//        return new ModelAndView("admin/users/index");
//    }

    @RequestMapping(value = "{id}/index.html", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable Long id) throws Exception {
        return new ModelAndView("admin/users/form", "command", dao.findById(id));
    }

    @RequestMapping(value = "{id}/index.html", method = RequestMethod.POST)
    public ModelAndView update(@PathVariable Long id, @Valid User entity) throws Exception {

        User original = dao.findById(id);
        entity.setId(original.getId());
        entity.setCreatedDate(original.getCreatedDate());
        service.createOrUpdate(entity);

        return new ModelAndView("admin/users/index");
    }

    @RequestMapping(value = "{id}/index.html", method = RequestMethod.DELETE)
    public ModelAndView delete(@PathVariable Long id) throws Exception {
        service.remove(dao.findById(id));
        return new ModelAndView("admin/users/index");
    }
}
