package com.smalaca.taskamanager.api.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import com.google.common.collect.Iterables;
import com.smalaca.taskamanager.dto.TeamDto;
import com.smalaca.taskamanager.dto.TeamMembersDto;
import com.smalaca.taskamanager.exception.TeamNotFoundException;
import com.smalaca.taskamanager.model.embedded.Codename;
import com.smalaca.taskamanager.model.entities.Team;
import com.smalaca.taskamanager.model.entities.User;
import com.smalaca.taskamanager.repository.TeamRepository;
import com.smalaca.taskamanager.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/team")
@SuppressWarnings("checkstyle:ClassFanOutComplexity")
public class TeamController {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamController(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<TeamDto>> findAll() {
        List<TeamDto> teams = StreamSupport.stream(teamRepository.findAll().spliterator(), false)
                .map(team -> {
                    TeamDto dto = new TeamDto();
                    dto.setId(team.getId());
                    dto.setName(team.getName());

                    if (team.getCodename() != null) {
                        dto.setCodenameShort(team.getCodename().getShortName());
                        dto.setCodenameFull(team.getCodename().getFullName());
                    }

                    dto.setDescription(team.getDescription());

                    return dto;
                })
                .collect(toList());

        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<TeamDto> findById(@PathVariable Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(team.get().toDto(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createTeam(@RequestBody TeamDto teamDto, UriComponentsBuilder uriComponentsBuilder) {
        if (teamRepository.findByName(teamDto.getName()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            Team team = new Team();
            team.setName(teamDto.getName());
            Team saved = teamRepository.save(team);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponentsBuilder.path("/team/{id}").buildAndExpand(saved.getId()).toUri());
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> updateTeam(@PathVariable Long id, @RequestBody TeamDto teamDto) {
        Team team;

        try {
            team = getTeamById(id);
        } catch (TeamNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (teamDto.getName() != null) {
            team.setName(teamDto.getName());
        }

        if (teamDto.getCodenameShort() != null && teamDto.getCodenameFull() != null) {
            Codename codename = new Codename(teamDto.getCodenameShort(), teamDto.getCodenameFull());
            team.setCodename(codename);
        }

        if (teamDto.getDescription() != null) {
            team.setDescription(teamDto.getDescription());
        }

        Team updated = teamRepository.save(team);

        TeamDto dto = new TeamDto();
        dto.setId(updated.getId());
        dto.setName(updated.getName());
        if (updated.getCodename() != null) {
            dto.setCodenameShort(updated.getCodename().getShortName());
            dto.setCodenameFull(updated.getCodename().getFullName());
        }

        dto.setDescription(updated.getDescription());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{id}/members")
    @Transactional
    public ResponseEntity<Void> addTeamMembers(@PathVariable Long id, @RequestBody TeamMembersDto dto) {
        try {
            Team team = getTeamById(id);
            Iterable<User> users = findUsers(dto);

            if (Iterables.size(users) != dto.getUserIds().size()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            users.forEach(user -> {
                user.addToTeam(team);
                team.addMember(user);
            });

            teamRepository.save(team);
            userRepository.saveAll(users);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (TeamNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/members")
    @Transactional
    public ResponseEntity<Void> removeTeamMembers(@PathVariable Long id, @RequestBody TeamMembersDto dto) {
        try {
            Team team = getTeamById(id);
            Iterable<User> users = findUsers(dto);

            users.forEach(user -> {
                if (user.getTeams().contains(team)) {
                    user.removeFrom(team);
                    team.removeMember(user);
                }
            });

            teamRepository.save(team);
            userRepository.saveAll(users);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (TeamNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Iterable<User> findUsers(TeamMembersDto dto) {
        return userRepository.findAllById(dto.getUserIds());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        Team team;

        try {
            team = getTeamById(id);
        } catch (TeamNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        teamRepository.delete(team);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Team getTeamById(Long id) {
        Optional<Team> team = teamRepository.findById(id);

        if (team.isEmpty()) {
            throw new TeamNotFoundException(id);
        }

        return team.get();
    }
}
