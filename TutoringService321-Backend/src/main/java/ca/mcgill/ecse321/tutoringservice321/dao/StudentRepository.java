package ca.mcgill.ecse321.tutoringservice321.dao;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.tutoringservice321.model.*;

public interface StudentRepository extends CrudRepository<Student, Integer>{
	
	Set<Student> findStudentBySession(Session session);
}
