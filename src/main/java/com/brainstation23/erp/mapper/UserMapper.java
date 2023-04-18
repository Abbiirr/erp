package com.brainstation23.erp.mapper;

import com.brainstation23.erp.model.domain.User;
import com.brainstation23.erp.model.dto.UserResponse;
import com.brainstation23.erp.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
	User entityToDomain(UserEntity entity);

	UserResponse domainToResponse(User User);

	default List<UserResponse> domainListToResponseList(List<User> users) {
        return users.stream().map(this::domainToResponse).collect(Collectors.toList());
    }
}
