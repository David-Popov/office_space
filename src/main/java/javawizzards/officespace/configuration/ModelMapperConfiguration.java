package javawizzards.officespace.configuration;

import javawizzards.officespace.dto.User.UserDto;
import javawizzards.officespace.entity.Role;
import javawizzards.officespace.entity.User;
import org.hibernate.collection.spi.PersistentBag;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        TypeMap<User, UserDto> typeMap = modelMapper.createTypeMap(User.class, UserDto.class);

        return modelMapper;
    }
}
