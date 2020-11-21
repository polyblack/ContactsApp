package com.polyblack.contactsapp.presentation.presenters.contact_details

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.polyblack.domain.entities.Contact
import com.polyblack.domain.interactors.calendar.NotificationCalendar
import com.polyblack.domain.interactors.contact_details.ContactDetailsInteractor
import com.polyblack.domain.interactors.notification.ContactNotificationInteractorImpl
import com.polyblack.domain.repositories.notification.ContactNotificationRepository
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.*

@SuppressLint("RestrictedApi")
object NotificationInstrumentedSpecification : Spek({
    val YEAR_1999 = 1999
    val YEAR_2000 = 2000
    val YEAR_2004 = 2004
    val FEBRUARY = 1
    val MARCH = 2
    val SEPTEMBER = 8
    val DAY_1 = 1
    val DAY_2 = 2
    val DAY_7 = 7
    val DAY_8 = 8
    val DAY_9 = 9
    val DAY_29 = 29
    val notificationCalendar: NotificationCalendar = mock()
    val contactNotificationRepository: ContactNotificationRepository = mock()
    val contactDetailsInteractor: ContactDetailsInteractor = mock()
    lateinit var currentDate: GregorianCalendar
    lateinit var birthdayDate: GregorianCalendar
    lateinit var contactDefault: Contact
    lateinit var contactExpected: Contact
    val contactNotificationInteractor =
        ContactNotificationInteractorImpl(contactNotificationRepository, notificationCalendar)
    val contactDetailsViewModel =
        ContactDetailsViewModel(contactDetailsInteractor, contactNotificationInteractor)

    beforeEachTest {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
    }

    afterEachTest { ArchTaskExecutor.getInstance().setDelegate(null) }

    fun setCurrentDate(year: Int, month: Int, day: Int) {
        currentDate = GregorianCalendar(year, month, day)
        Mockito.`when`(notificationCalendar.getCalendar())
            .thenReturn(currentDate)
    }

    fun setContact(name: String, birthday: String, isNotificationOn: Boolean) {
        contactDefault = Contact(1, name, birthday = birthday, isNotificationOn = isNotificationOn)
    }

    fun setMockNotificationStatus(status: Boolean) {
        Mockito.`when`(contactNotificationRepository.getNotificationStatus(contactDefault))
            .thenReturn(status)
    }

    fun setBirthday(year: Int, month: Int, day: Int) {
        birthdayDate = GregorianCalendar(year, month, day)
    }

    fun setExpectedNotificationStatus(status: Boolean) {
        contactExpected = contactDefault
        contactExpected.isNotificationOn = status
        if (status) {
            Mockito.`when`(
                contactNotificationRepository.setNotification(
                    contactDefault,
                    birthdayDate.timeInMillis
                )
            )
                .thenReturn(contactExpected)
        } else {
            Mockito.`when`(
                contactNotificationRepository.cancelNotification(
                    contactDefault
                )
            )
                .thenReturn(contactExpected)
        }
    }

    Feature("Напоминание") {

        Scenario("Успешное добавление напоминания") {
            Given("Текущий год - 1999(не високосный) 9 сентября") {
                setCurrentDate(YEAR_1999, SEPTEMBER, DAY_9)
            }
            And("Есть контакт Иван Иванович с датой рождения 8 сентября") {
                setContact("Иван Иванович", "--08-09", false)
                setBirthday(YEAR_2000, SEPTEMBER, DAY_8)
            }
            And("И напоминание для этого контакта отсутствует") {
                setMockNotificationStatus(false)
            }
            When("Когда пользователь кликает на кнопку напоминания в детальной информации контакта Иван Иванович") {
                setExpectedNotificationStatus(true)
                contactDetailsViewModel.getContactWithNewNotificationStatus(contactDefault)
            }
            Then("Тогда  происходит успешное добавление напоминания на 2000 год 8 сентября") {
                verify(contactNotificationRepository).setNotification(
                    contactDefault,
                    birthdayDate.timeInMillis
                )
            }

        }

        Scenario("Успешное добавление напоминания, ДР еще в текущем году не было") {
            Given("Текущий год - 1999(не високосный) 7 сентября") {
                setCurrentDate(YEAR_1999, SEPTEMBER, DAY_7)
            }
            And("Есть контакт Иван Иванович с датой рождения 8 сентября") {
                setContact("Иван Иванович", "--08-09", false)
                setBirthday(YEAR_1999, SEPTEMBER, DAY_8)
            }
            And("И напоминание для этого контакта отсутствует") {
                setMockNotificationStatus(false)
            }
            When("Когда пользователь кликает на кнопку напоминания в детальной информации контакта Иван Иванович") {
                setExpectedNotificationStatus(true)
                contactDetailsViewModel.getContactWithNewNotificationStatus(contactDefault)
            }
            Then("Тогда  происходит успешное добавление напоминания на 1999 год 8 сентября") {
                verify(contactNotificationRepository).setNotification(
                    contactDefault,
                    birthdayDate.timeInMillis
                )
            }
        }

        Scenario("Успешное удаление напоминания") {
            Given("Текущий год - 1999(не високосный)") {
                setCurrentDate(YEAR_1999, SEPTEMBER, DAY_7)
            }
            And("Есть контакт Иван Иванович с датой рождения 8 сентября") {
                setContact("Иван Иванович", "--08-09", false)
                setBirthday(YEAR_1999, SEPTEMBER, DAY_8)
            }
            And("И для него включено напоминание на 2000 год 8 сентября") {
                setMockNotificationStatus(true)
            }
            When("Когда пользователь кликает на кнопку напоминания в детальной информации контакта Иван Иванович") {
                setExpectedNotificationStatus(false)
                contactDetailsViewModel.getContactWithNewNotificationStatus(contactDefault)
            }
            Then("Тогда  происходит успешное удаление напоминания") {
                verify(contactNotificationRepository).cancelNotification(contactDefault)
            }
        }

        Scenario("Добавление напоминания для контакта родившегося 29 февраля") {
            Given("Текущий год - 1999(не високосный), следующий 2000(високосный) 2 марта")
            {
                setCurrentDate(YEAR_1999, MARCH, DAY_2)
            }
            And("Есть контакт Павел Павлович с датой рождения 29 февраля") {
                setContact("Павел Павлович", "--29-02", false)
                setBirthday(YEAR_2000, FEBRUARY, DAY_29)
            }
            And("И напоминание для этого контакта отсутствует") {
                setMockNotificationStatus(false)
            }
            When("Когда пользователь кликает на кнопку напоминания в детальной информации контакта Павел Павлович") {
                setExpectedNotificationStatus(true)
                contactDetailsViewModel.getContactWithNewNotificationStatus(contactDefault)
            }
            Then("Тогда  происходит успешное добавление напоминания на 2000 год 29 февраля") {
                verify(contactNotificationRepository).setNotification(
                    contactDefault,
                    birthdayDate.timeInMillis
                )
            }
        }

        Scenario("Добавление напоминания для контакта родившегося 29 февраля в високосный год") {
            Given("Текущий год - 2000(високосный) 1 марта")
            {
                setCurrentDate(YEAR_2000, MARCH, DAY_1)
            }
            And("Есть контакт Павел Павлович с датой рождения 29 февраля") {
                setContact("Павел Павлович", "--29-02", false)
                setBirthday(YEAR_2004, FEBRUARY, DAY_29)
            }
            And("И напоминание для этого контакта отсутствует") {
                setMockNotificationStatus(false)
            }
            When("Когда пользователь кликает на кнопку напоминания в детальной информации контакта Павел Павлович") {
                setExpectedNotificationStatus(true)
                contactDetailsViewModel.getContactWithNewNotificationStatus(contactDefault)
            }
            Then("Тогда  происходит успешное добавление напоминания на 2004 год 29 февраля") {
                verify(contactNotificationRepository).setNotification(
                    contactDefault,
                    birthdayDate.timeInMillis
                )
            }
        }
    }
})
