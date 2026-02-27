package uk.ac.ncl.csc8019backend.business.demo.controller;

import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.csc8019backend.business.demo.entity.Demo;
import uk.ac.ncl.csc8019backend.business.demo.service.DemoService;
import uk.ac.ncl.csc8019backend.system.common.Result;

import java.util.List;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/list")
    public Result<List<Demo>> list() {
        return Result.success(demoService.getAllDemos());
    }

    @GetMapping("/{id}")
    public Result<Demo> get(@PathVariable Long id) {
        return Result.success(demoService.getDemoById(id));
    }

    @PostMapping("/create")
    public Result<Demo> create(@RequestBody Demo demo) {
        return Result.success(demoService.createDemo(demo));
    }
}
