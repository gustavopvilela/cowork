package backend.coworking.resource;

import backend.coworking.dto.AvaliacaoDTO;
import backend.coworking.dto.EspacoStatsDTO;
import backend.coworking.dto.insert.AvaliacaoInsertDTO;
import backend.coworking.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping
@Tag(name = "Avaliação", description = "Controller para avaliações")
public class AvaliacaoResource {
    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping("/reserva/{id}/avaliar")
    @Operation(
            summary = "Insere uma avaliação para uma reserva concluída",
            description = "Quando uma reserva for concluída, o usuário que reservou tem a opção de deixar uma avaliação.",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<AvaliacaoDTO> insert (@PathVariable Long id, @Valid @RequestBody AvaliacaoInsertDTO dto) {
        AvaliacaoDTO aDTO = avaliacaoService.insert(id, dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(aDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(aDTO);
    }

    @PutMapping("avaliacao/{id}")
    @Operation(
            summary = "Atualiza uma avaliação",
            description = "Atualiza uma avaliação existente. Apenas o autor da avaliação pode realizar esta operação.",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_PROFISSIONAL', 'ROLE_ADMIN')")
    public ResponseEntity<AvaliacaoDTO> update (@PathVariable Long id, @Valid @RequestBody AvaliacaoInsertDTO dto) {
        AvaliacaoDTO aDTO = avaliacaoService.update(id, dto);
        return ResponseEntity.ok().body(aDTO);
    }

    @DeleteMapping("avaliacao/{id}")
    @Operation(
            summary = "Deleta uma avaliação",
            description = "Deleta uma avaliação existente. Apenas administradores e os autores das avaliações podem realizar essa operação.",
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        avaliacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/espaco/{id}/avaliacoes")
    @Operation(
            summary = "Busca as avaliações de um espaço específico",
            description = "Encontra todas as avaliações de reservas feitas em um determinado espaço",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            }
    )
    public ResponseEntity<Page<AvaliacaoDTO>> findByEspaco (@PathVariable Long id, Pageable pageable) {
        Page<AvaliacaoDTO> page = avaliacaoService.findByEspaco(id, pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/espaco/{id}/avaliacoes/status")
    @Operation(
            summary = "Busca o status das avaliações de um espaço",
            description = "Retorna a contagem de cada nota e a média geral das notas para um espaço específico.",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            }
    )
    public ResponseEntity<EspacoStatsDTO> getEspacoStats (@PathVariable Long id) {
        EspacoStatsDTO stats = avaliacaoService.getEspacoStats(id);
        return ResponseEntity.ok().body(stats);
    }

    @GetMapping("/avaliacao/me")
    @Operation(
            summary = "Busca as avaliações do usuário autenticado",
            description = "Dado um usuário logado no sistema, busca todas as avaliações dele",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<Page<AvaliacaoDTO>> findMinhasAvaliacoes(Pageable pageable) {
        Page<AvaliacaoDTO> page = avaliacaoService.findMinhasAvaliacoes(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/usuario/{id}/avaliacoes")
    @Operation(
            summary = "Busca as avaliações de um usuário",
            description = "Dado um ID de usuário, busca todas as avaliações dele",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<AvaliacaoDTO>> findByUsuario(@PathVariable Long id, Pageable pageable) {
        Page<AvaliacaoDTO> page = avaliacaoService.findByUsuario(id, pageable);
        return ResponseEntity.ok().body(page);
    }
}
