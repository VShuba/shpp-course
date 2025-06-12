package shpp.shuba.spring_jpa_first.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shpp.shuba.spring_jpa_first.models.Employee;

import java.util.Optional;

@Repository
public interface EmployeeRepositoryInterface extends JpaRepository<Employee, Long> {
    Optional<Employee> findByTaxIdNumber(String taxNumber);
    Optional<Employee> findByEmail(String email);

    Page<Employee> findAllBy(Pageable pageable);
}
/*
                це вміє по дефолту
  //  Читання
List<Book> findAll(); // Отримати всі книги
Optional<Book> findById(Long id); // Знайти книгу за ID
boolean existsById(Long id); // Перевірити, чи існує книга з таким ID
long count(); // Отримати кількість книг

  //  Створення та оновлення
Book save(Book book); // Зберегти або оновити книгу
List<Book> saveAll(Iterable<Book> books); // Зберегти список книг

 //   Видалення
void deleteById(Long id); // Видалити книгу за ID
void delete(Book book); // Видалити конкретну книгу
void deleteAll(); // Видалити всі книги


    !!! important -> Для UPDATE і DELETE потрібен @Modifying + @Transactional

    @Query("DELETE FROM Book b WHERE b.isbn = :isbn")
    @Transactional
    @Modifying
    void deleteByIsbn(@Param("isbn") String isbn);
 */