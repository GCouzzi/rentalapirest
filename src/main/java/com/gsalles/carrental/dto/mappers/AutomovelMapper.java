package com.gsalles.carrental.dto.mappers;

import com.gsalles.carrental.dto.AutomovelDTO;
import com.gsalles.carrental.dto.rdto.AutomovelResponseDTO;
import com.gsalles.carrental.entity.Automovel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AutomovelMapper {

    public static Automovel toAutomovel(AutomovelDTO automovelDTO){
        return new ModelMapper().map(automovelDTO, Automovel.class);
    }

    public static AutomovelResponseDTO toDto(Automovel automovel){
        return new ModelMapper().map(automovel, AutomovelResponseDTO.class);
    }

    public static Page<AutomovelResponseDTO> toListDto(Page<Automovel> list){
        return list.map(AutomovelMapper::toDto);
    }
}
