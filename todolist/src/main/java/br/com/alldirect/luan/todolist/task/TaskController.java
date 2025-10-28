package br.com.alldirect.luan.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody TaskModel taskModel) {
        LocalDateTime now = LocalDateTime.now();

        if (taskModel.getStartAt() == null) {
            return badRequest("START_AT_REQUIRED", "startAt é obrigatório");
        }

        // ❗ Regra: NÃO permitir data no futuro
        if (taskModel.getStartAt().isAfter(now)) {
            return badRequest("START_AT_IN_FUTURE", "a data de início não pode ser no futuro");
        }

        // (Opcional) Aplique a mesma regra a endAt, se fizer sentido:


        // (Opcional) Garanta ordem cronológica, se necessário:
        if (taskModel.getEndAt() != null && taskModel.getEndAt().isBefore(taskModel.getStartAt())) {
            return badRequest("END_BEFORE_START", "a data de término não pode ser anterior ao início");
        }

        var saved = taskRepository.save(taskModel);
        return ResponseEntity.created(URI.create("/tasks/" + saved.getId())).body(saved);
    }

    private ResponseEntity<Map<String, Object>> badRequest(String code, String message) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", code,
                "message", message,
                "status", 400,
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
