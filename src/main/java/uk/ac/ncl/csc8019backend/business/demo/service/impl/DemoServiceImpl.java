package uk.ac.ncl.csc8019backend.business.demo.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.ncl.csc8019backend.business.demo.entity.Demo;
import uk.ac.ncl.csc8019backend.business.demo.repository.DemoRepository;
import uk.ac.ncl.csc8019backend.business.demo.service.DemoService;
import uk.ac.ncl.csc8019backend.system.exception.CustomException;

import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {

    private final DemoRepository demoRepository;

    public DemoServiceImpl(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    @Override
    public List<Demo> getAllDemos() {
        return demoRepository.findAll();
    }

    @Override
    public Demo getDemoById(Long id) {
        return demoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Demo data not found with id: " + id));
    }

    @Override
    public Demo createDemo(Demo demo) {
        return demoRepository.save(demo);
    }
}
