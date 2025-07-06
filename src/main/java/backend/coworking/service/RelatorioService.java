package backend.coworking.service;

import backend.coworking.dto.ServicoRankingDTO;
import backend.coworking.entity.Reserva;
import backend.coworking.repository.EspacoRepository;
import backend.coworking.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    public Map<String, Double> getTaxaOcupacao (int ano, int mes) {
        YearMonth ym = YearMonth.of(ano, mes);
        LocalDateTime inicioMesLocal = ym.atDay(1).atStartOfDay();
        LocalDateTime fimMesLocal = ym.atEndOfMonth().atTime(23, 59, 59);

        Instant inicioMes = inicioMesLocal.toInstant(ZoneOffset.UTC);
        Instant fimMes = fimMesLocal.toInstant(ZoneOffset.UTC);

        List<Reserva> reservasNoMes = reservaRepository.findAllByPeriodo(inicioMes, fimMes);

        long totalSegundosReservados = 0;
        for (Reserva r : reservasNoMes) {
            totalSegundosReservados += Duration.between(r.getEntrada(), r.getSaida()).getSeconds();
        }
        double totalHorasReservadas = totalSegundosReservados / 3600.0;

        long dias = ym.lengthOfMonth();
        long totalHorasPossiveis = espacoRepository.count() * dias * 24;

        double taxaOcupacao = (totalHorasPossiveis > 0) ? (totalHorasReservadas / totalHorasPossiveis) : 0.0;
        taxaOcupacao = taxaOcupacao * 100;
        taxaOcupacao = BigDecimal.valueOf(taxaOcupacao).setScale(3, RoundingMode.HALF_UP).doubleValue();

        return Map.of("taxa_ocupacao_percentual", taxaOcupacao);
    }

    public List<ServicoRankingDTO> getTopServicosMaisUsados () {
        return reservaRepository.findTopServicos();
    }
}
