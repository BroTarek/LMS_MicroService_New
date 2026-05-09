$ErrorActionPreference = "Stop"
$BASE = "http://localhost:8080"

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "      LMS MICROSERVICE API TESTER        " -ForegroundColor Cyan
Write-Host "=========================================`n" -ForegroundColor Cyan

# Helper to format JSON output
function Format-Output {
    param($data)
    $data | ConvertTo-Json -Depth 5 | Write-Host -ForegroundColor DarkGray
}

try {
    Write-Host "1. Registering Teacher (mr_test)..." -ForegroundColor Green
    $teacherData = @{
        username = "mr_test"
        password = "password123"
        role = "TEACHER"
        fullName = "Mr Test"
        email = "mrtest@test.com"
    } | ConvertTo-Json
    Invoke-RestMethod -Uri "$BASE/auth/register" -Method Post -Body $teacherData -ContentType "application/json" | Out-Null
    Write-Host "   -> Teacher registered successfully.`n"

    Write-Host "2. Registering Student (student_test)..." -ForegroundColor Green
    $studentData = @{
        username = "student_test"
        password = "password123"
        role = "STUDENT"
        fullName = "Student Test"
        email = "student@test.com"
    } | ConvertTo-Json
    Invoke-RestMethod -Uri "$BASE/auth/register" -Method Post -Body $studentData -ContentType "application/json" | Out-Null
    Write-Host "   -> Student registered successfully.`n"

    Write-Host "3. Logging in as Teacher..." -ForegroundColor Green
    $loginData = @{ username = "mr_test"; password = "password123" } | ConvertTo-Json
    $teacherLogin = Invoke-RestMethod -Uri "$BASE/auth/login" -Method Post -Body $loginData -ContentType "application/json"
    $teacherToken = $teacherLogin.token
    Write-Host "   -> Logged in. Token received.`n"

    Write-Host "4. Creating a Course..." -ForegroundColor Green
    $courseData = @{ title = "API Testing 101"; description = "Automated testing via script" } | ConvertTo-Json
    $course = Invoke-RestMethod -Uri "$BASE/api/courses" -Method Post -Body $courseData -ContentType "application/json" -Headers @{ Authorization = "Bearer $teacherToken" }
    Format-Output $course
    $courseId = $course.id
    Write-Host ""

    Write-Host "5. Adding a Lesson to Course $courseId..." -ForegroundColor Green
    $lessonData = @{ title = "Introduction"; contentUrl = "http://example.com/vid"; orderIndex = 1 } | ConvertTo-Json
    $lesson = Invoke-RestMethod -Uri "$BASE/api/courses/$courseId/lessons" -Method Post -Body $lessonData -ContentType "application/json" -Headers @{ Authorization = "Bearer $teacherToken" }
    Format-Output $lesson
    Write-Host ""

    Write-Host "6. Logging in as Student..." -ForegroundColor Green
    $loginDataStudent = @{ username = "student_test"; password = "password123" } | ConvertTo-Json
    $studentLogin = Invoke-RestMethod -Uri "$BASE/auth/login" -Method Post -Body $loginDataStudent -ContentType "application/json"
    $studentToken = $studentLogin.token
    Write-Host "   -> Logged in. Token received.`n"

    Write-Host "7. Student Requesting Enrollment in Course $courseId..." -ForegroundColor Green
    $enrollData = @{ courseId = $courseId } | ConvertTo-Json
    $enrollment = Invoke-RestMethod -Uri "$BASE/api/enrollments" -Method Post -Body $enrollData -ContentType "application/json" -Headers @{ Authorization = "Bearer $studentToken" }
    Format-Output $enrollment
    Write-Host ""

    Write-Host "8. Teacher Getting Pending Enrollments..." -ForegroundColor Green
    $pending = Invoke-RestMethod -Uri "$BASE/api/enrollments/pending?courseId=$courseId" -Method Get -Headers @{ Authorization = "Bearer $teacherToken" }
    Format-Output $pending
    $enrollmentId = $pending[0].id
    Write-Host ""

    Write-Host "9. Teacher Approving Enrollment $enrollmentId..." -ForegroundColor Green
    Invoke-RestMethod -Uri "$BASE/api/enrollments/$enrollmentId/approve" -Method Post -Headers @{ Authorization = "Bearer $teacherToken" } | Out-Null
    Write-Host "   -> Approved successfully.`n"

    Write-Host "10. Student Viewing Enrolled Courses..." -ForegroundColor Green
    $myCourses = Invoke-RestMethod -Uri "$BASE/api/enrollments/my-courses" -Method Get -Headers @{ Authorization = "Bearer $studentToken" }
    Format-Output $myCourses
    Write-Host "`nAll API tests completed successfully!" -ForegroundColor Cyan

} catch {
    Write-Host "ERROR: $_" -ForegroundColor Red
}
