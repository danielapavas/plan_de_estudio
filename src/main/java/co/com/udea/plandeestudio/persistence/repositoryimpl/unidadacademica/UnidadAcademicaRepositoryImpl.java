package co.com.udea.plandeestudio.persistence.repositoryimpl.unidadacademica;

import co.com.udea.plandeestudio.domain.model.UnidadAcademica;
import co.com.udea.plandeestudio.domain.repository.unidadacademica.UnidadAcademicaRepository;
import co.com.udea.plandeestudio.persistence.crud.UnidadAcademicaCrud;
import co.com.udea.plandeestudio.persistence.entity.UnidadAcademicaEntity;
import co.com.udea.plandeestudio.persistence.mapper.PersonaMapper;
import co.com.udea.plandeestudio.persistence.mapper.UnidadAcademicaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UnidadAcademicaRepositoryImpl implements UnidadAcademicaRepository {
    private final UnidadAcademicaCrud persistence;
    private final UnidadAcademicaMapper mapper;
    private final PersonaMapper mapperPersona;

    @Autowired
    public UnidadAcademicaRepositoryImpl(UnidadAcademicaCrud persistence, UnidadAcademicaMapper mapper, PersonaMapper mapperPersona) {
        this.persistence = persistence;
        this.mapper = mapper;
        this.mapperPersona = mapperPersona;
    }


    @Override
    public Optional<UnidadAcademica> getUnidadAcademicaByCodigo(String codigo) {
        return persistence.findUnidadAcademicaEntitiesByCodigo(codigo)
                .map(mapper::toUnidadAcademica);
    }

    @Override
    public Optional<List<UnidadAcademica>> getAllUnidadAcademica() {
        List<UnidadAcademicaEntity> lista = persistence.findAll();
        if (lista.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.tounidadesAcademicas(lista));
    }

    @Override
    public Optional<UnidadAcademica> save(UnidadAcademica unidadAcademica) {
        UnidadAcademicaEntity entity = mapper.toUnidadAcademicaEntity(unidadAcademica);
        return Optional.of(mapper.toUnidadAcademica(persistence.save(entity)));
    }

    @Override
    public Optional<UnidadAcademica> update(UnidadAcademica unidadAcademica) {
        Optional<UnidadAcademicaEntity> entity = persistence.findUnidadAcademicaEntitiesByCodigo(unidadAcademica.getCodigo());

        if (!entity.isPresent()) {
            return Optional.empty();
        }

        entity.get().setDescripcion(unidadAcademica.getDescripcion());
        entity.get().setDecano(mapperPersona.toPersonaEntity(unidadAcademica.getDecano()));

        return Optional.of(mapper.toUnidadAcademica(persistence.save(entity.get())));
    }

    @Override
    public void delete(String codigo) {
        persistence.deleteUnidadAcademicaEntityByCodigo(codigo);
    }
}
