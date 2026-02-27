package uk.ac.ncl.csc8019backend.business.demo.service;

import uk.ac.ncl.csc8019backend.business.demo.entity.Demo;
import java.util.List;

public interface DemoService {
    List<Demo> getAllDemos();
    Demo getDemoById(Long id);
    Demo createDemo(Demo demo);
}
