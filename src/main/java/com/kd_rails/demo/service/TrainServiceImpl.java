package com.kd_rails.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kd_rails.demo.dto.TrainDTO;
import com.kd_rails.demo.entity.Train;
import com.kd_rails.demo.exception.RouteDoesNotExistException;
import com.kd_rails.demo.exception.TrainAlreadyExistsException;
import com.kd_rails.demo.repository.RouteRepository;
import com.kd_rails.demo.repository.TrainRepository;
import com.kd_rails.demo.utility.TrainMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Override
    public TrainDTO createTrain(TrainDTO trainDTO) {
        log.info("Creating Train with Train Name: {}, Route ID: {}", trainDTO.getTrainName(), trainDTO.getRouteId());

        if (trainRepository.existsByTrainNameAndRouteID(trainDTO.getTrainName(), trainDTO.getRouteId())) {
            throw new TrainAlreadyExistsException(trainDTO.getTrainName(), trainDTO.getRouteId());
        } else if (!routeRepository.existsById(trainDTO.getRouteId())) {
            throw new RouteDoesNotExistException(trainDTO.getRouteId());
        }

        Train train = TrainMapper.toEntity(trainDTO);
        Train savedTrain = trainRepository.save(train);
        return TrainMapper.toDTO(savedTrain);
    }

}
