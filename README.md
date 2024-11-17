1.	Introduction
The Online Exam Portal is a secure and user-friendly Android app designed to facilitate online examinations. It allows students to take exams and professors to upload and evaluate exam papers. The app ensures secure login for both professors and students, provides options for multiple-choice, subjective, and programming exams, and allows for automated as well as manual grading.
Key Features:
1.	Student Module:
o	Secure Login/Registration: User authentication with Firebase Authentication (email/password, Google sign-in).
o	Exam Dashboard: List of available exams with time, date, and subject.
o	Take Exam: Supports multiple-choice questions (MCQs), true/false, fill-in-the-blanks, and descriptive questions.
o	Timer: Countdown for exam duration with auto-submission after time expiration.
o	Submit Exam: Upload the answers securely, even in low network conditions.
o	View Results: Students can view detailed results once published by the professors.
2.	Professor Module:
o	Secure Login: Firebase authentication for professors.
o	Create Exam: Professors can create exams by uploading questions, setting time limits, and specifying exam types.
o	Upload Questions: Upload exam questions (MCQ, short answer, or long-answer types) using Retrofit for seamless server communication.
o	Evaluate Papers: View submitted answers and mark subjective questions manually.
o	Publish Results: Once grading is done, professors can publish the results, which will be visible to students.
Tech Stack:
1.	Firebase:
o	Firebase Authentication: Handles secure user login and registration for both students and professors.
o	Firebase Fire store: Real-time NoSQL database to store exam details, user data, and results.
o	Firebase Storage: For storing exam papers, media files, or attachments uploaded by professors or students.
2.	Retrofit:
o	API Integration: Retrofit is used for seamless API communication between the Android app and the backend server (if any external APIs are required for additional features).
o	RESTful API: Interact with external services or upload/download exam-related data.
Workflow:
1.	Login/Registration: Students and professors sign in using Firebase Authentication.
2.	Exam Creation (Professor):
o	Create and upload exam papers using a user-friendly interface.
o	Set time limits, types of questions, and other exam configurations.
3.	Taking Exams (Student):
o	View available exams on the dashboard.
o	Answer questions (MCQs, descriptive, etc.) within the given time frame.
o	Submit the exam securely using Firebase Firestore.
4.	Result Evaluation (Professor):
o	Evaluate answers, auto-grade MCQs, and manually review descriptive answers.
o	Publish results that are stored in Firebase fire store and displayed on the student’s dashboard.
