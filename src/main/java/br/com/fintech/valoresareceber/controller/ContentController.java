package br.com.fintech.valoresareceber.controller;

import br.com.fintech.valoresareceber.dto.*;
import br.com.fintech.valoresareceber.service.FirestoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

    @Autowired
    private FirestoreService firestoreService;

    @GetMapping("/guides")
    public ResponseEntity<List<GuideStepDTO>> getGuideSteps(@RequestHeader("X-Session-ID") String sessionId) throws ExecutionException, InterruptedException {
    logger.info("Buscando 'guides' para a sessão: {}", sessionId);
    return ResponseEntity.ok(firestoreService.getGuideSteps());
    }

    @GetMapping("/faqs")
    public ResponseEntity<List<FaqsDTO>> getFaqs(@RequestHeader("X-Session-ID") String sessionId) throws ExecutionException, InterruptedException {
        logger.info("Buscando 'faqs' para a sessão: {}", sessionId);
        return ResponseEntity.ok(firestoreService.getFaqs());
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getProducts(@RequestHeader("X-Session-ID") String sessionId) throws ExecutionException, InterruptedException {
        logger.info("Buscando 'products' para a sessão: {}", sessionId);
        return ResponseEntity.ok(firestoreService.getProducts());
    }

    @GetMapping("/gov-requirements")
    public ResponseEntity<List<InstructionalCardDTO>> getGovRequirements(@RequestHeader("X-Session-ID") String sessionId) throws ExecutionException, InterruptedException {
        logger.info("Buscando 'gov-requirements' para a sessão: {}", sessionId);
        return ResponseEntity.ok(firestoreService.getGovRequirements());
    }
}