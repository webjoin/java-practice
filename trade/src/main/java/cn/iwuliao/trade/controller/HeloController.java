package cn.iwuliao.trade.controller;

import cn.iwuliao.trade.service.HeloService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author tangyu
 * @since 2019-04-22 20:30
 */
@RestController
@RequestMapping("/hi")
public class HeloController {


    private HeloService heloService;

    public HeloController(HeloService heloService) {
        this.heloService = heloService;
    }


    @RequestMapping("/hi")
    public String hi() {

        Integer hia = heloService.hia();
        Integer hib = heloService.hib();

        return "hia:" + hia + "<-->hib:" + hib;
    }


    @RequestMapping("/tx/{commit}")
    public String tx(@PathVariable("commit") String commit) {

        if ("commit".equals(commit)) {
            Integer commit1 = heloService.commit();
            System.out.println("it has commited ? " + (commit1 > 0));
        } else {
            Integer commit1 = 0;
            try {
                commit1 = heloService.rollback();
            } finally {
                System.out.println("it is rollback ? " + (commit1 > 0));
            }
        }
        return "i am tx ";
    }


    @RequestMapping("/like")
    public List<String> like(@RequestParam(name = "name") String name) {
        return heloService.likeQry(name);
    }

}
