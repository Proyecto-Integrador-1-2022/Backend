package co.udea.hero.api.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.udea.hero.api.exception.DataException;
import co.udea.hero.api.model.Hero;
import co.udea.hero.api.repository.HeroRepository;
import co.udea.hero.api.service.HeroService;
import co.udea.hero.api.util.Messages;

@Service
public class HeroServiceImp implements HeroService {

    private final Logger log = LoggerFactory.getLogger(HeroServiceImp.class);

    private Messages messages;
    private HeroRepository heroRepository;

    public HeroServiceImp(HeroRepository heroRepository, Messages messages) {
        this.heroRepository = heroRepository;
        this.messages = messages;
    }

    @Override
    public List<Hero> getHeroes() {
        log.debug("Iniciciando getHeroes");
        List<Hero> heroes= heroRepository.findAll();
        log.debug("Finalizando getHeroes");
        return heroes;
    }

    @Override
    public Hero getHero(int id) {
        log.debug("Iniciando getHero: id = {}", id);
        Optional<Hero> hero = heroRepository.findById(id);
        if (!hero.isPresent()) {
            throw new DataException(messages.get("exception.data_not_found.hero"));
        }
        log.debug("Finalizando getHero: heroe = {}", hero.get());
        return hero.get();
    }

    @Override
    public List<Hero> searchHeroes(String nombre) {
        log.debug("Iniciando Busqueda Heroe: nombre = {}", nombre);
        List<Hero> heroes = new ArrayList<Hero>();

        for(Hero heroe : heroRepository.findAll()) {
            if(heroe.getName().contains(nombre)) {
                heroes.add(heroe);
            }
        }
        return heroes;
    }

    public void deleteHero(int id) {
        log.debug("Borrando Heroe: id = {}", id);
        Optional<Hero> hero = heroRepository.findById(id);
        if(!hero.isPresent()) {
            throw new DataException(messages.get("exception.data_not_found.hero"));
        }
        heroRepository.delete(hero.get());
        return;
    }

    public Hero updateHero(int id, String nuevoNombre){
        Optional<Hero> hero = heroRepository.findById(id);
        if(!hero.isPresent()) {
            throw new DataException(messages.get("exception.data_not_found.hero"));
        }
        heroRepository.findById(id).map(heroe -> {
            heroe.setName(nuevoNombre);
            return heroRepository.save(heroe);
        });
        return hero.get();
    }

    public Hero crearHero(int id, String nombre) {
        Optional<Hero> hero = heroRepository.findById(id);
        if(hero.isPresent()) {
            throw new DataException(messages.get("exception.already_exists.hero"));
        }
        Hero heroe = new Hero(id,nombre);
        heroRepository.save(heroe);
        return heroe;
    }

    public Hero getHeroNo404(int id) {
        log.debug("Iniciando getHeroNo404: id = {}", id);
        Optional<Hero> hero = heroRepository.findById(id);
        if(!hero.isPresent()){
            throw new DataException(messages.get("exception.data_not_found.hero404"));
        }
        log.debug("Fin getHeroNo404: heroe = {}", hero.get());
        return hero.get();
    }
}
