package net.iqbalfauzan.belajarspringbootrestfulapi.service;

import jakarta.persistence.criteria.Predicate;
import net.iqbalfauzan.belajarspringbootrestfulapi.entity.Contact;
import net.iqbalfauzan.belajarspringbootrestfulapi.entity.User;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.response.ContactResponse;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.request.CreateContactRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.request.SearchContactRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.request.UpdateContactRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ContactService {

    @Autowired
    private ContactRepository repository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ContactResponse create(User user, CreateContactRequest request) {
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstname(request.getFirstName());
        contact.setLastname(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        repository.save(contact);

        return toContactResponse(contact);
    }

    private ContactResponse toContactResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstname())
                .lastName(contact.getLastname())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional(readOnly = true)
    public ContactResponse get(User user, String id) {
        Contact contact = repository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        return toContactResponse(contact);
    }

    @Transactional
    public ContactResponse update(User user, UpdateContactRequest updateContactRequest) {
        validationService.validate(updateContactRequest);
        Contact contact = repository.findFirstByUserAndId(user, updateContactRequest.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstname(updateContactRequest.getFirstName());
        contact.setLastname(updateContactRequest.getLastName());
        contact.setPhone(updateContactRequest.getPhone());
        contact.setEmail(updateContactRequest.getEmail());
        repository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional
    public void delete(User user, String contactId) {
        Contact contact = repository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        repository.delete(contact);
    }

    @Transactional(readOnly = true)
    public Page<ContactResponse> search(User user, SearchContactRequest request) {
        Specification<Contact> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("user"), user));
            if (Objects.nonNull(request.getName())) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("firstname"), "%" + request.getName() + "%"),
                        criteriaBuilder.like(root.get("lastname"), "%" + request.getName() + "%")
                ));
            }
            if (Objects.nonNull(request.getEmail())) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("email"), "%" + request.getEmail() + "%")
                ));
            }
            if (Objects.nonNull(request.getPhone())) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("phone"), "%" + request.getPhone() + "%")
                ));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = repository.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts.getContent().stream()
                .map(this::toContactResponse)
                .toList();

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }
}
