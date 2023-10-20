package com.generation.sistemafarmacia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.sistemafarmacia.model.Remedio;
import com.generation.sistemafarmacia.repository.RemedioRepository;
import com.generation.sistemafarmacia.repository.CategoriaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/remedio")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RemedioController {

    @Autowired
    private RemedioRepository remedioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<List<Remedio>> getAll(){
        return ResponseEntity.ok(remedioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Remedio> getById(@PathVariable Long id){
        return remedioRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Remedio>> getByNome(@PathVariable String nome){
        return ResponseEntity.ok(remedioRepository.findAllByNomeContainingIgnoreCase(nome));
    }

    @PostMapping
    public ResponseEntity<Remedio> post(@Valid @RequestBody Remedio remedio){
        if(categoriaRepository.existsById(remedio.getCategoria().getId()))
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(remedioRepository.save(remedio));

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria inexistente!", null);
    }

    @PutMapping
    public ResponseEntity<Remedio> put(@Valid @RequestBody Remedio remedio){
        if (remedioRepository.existsById(remedio.getId())){

            if (categoriaRepository.existsById(remedio.getCategoria().getId()))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(remedioRepository.save(remedio));

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria inexistente!", null);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Remedio> remedio = remedioRepository.findById(id);

        if(remedio.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        remedioRepository.deleteById(id);
    }

}
