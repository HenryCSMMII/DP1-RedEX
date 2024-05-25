package com.edu.pucp.dp1.redex.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/algorithm")
@CrossOrigin
public class AsignacionVuelosController {

    @PostMapping(value = "/post/asignarVuelos")
    @ResponseBody
    public int AsignarVuelos(){

    }


}
