from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.contrib import messages
from django.contrib.auth.models import User
from django.core.paginator import Paginator

from .models import Student
from .forms import StudentForm, RegisterForm


def home(request):
    return render(request, 'home.html')


def register_view(request):
    if request.method == 'POST':
        form = RegisterForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('login')
    else:
        form = RegisterForm()
    return render(request, 'register.html', {'form': form})


def login_view(request):
    if request.method == 'POST':
        uname = request.POST['username']
        pwd = request.POST['password']
        user = authenticate(request, username=uname, password=pwd)
        if user is not None:
            login(request, user)
            if user.is_superuser:
                messages.success(request, "Admin login successful!")
                return redirect('student_list')
            else:
                messages.success(request, "User login successful!")
                return redirect('student_listuser')
        else:
            messages.error(request, 'Invalid credentials')
            return redirect('login')
    return render(request, 'login.html')


def logout_view(request):
    logout(request)
    return redirect('login')


@login_required
def student_list(request):
    query = request.GET.get('q')
    if query:
        students = Student.objects.filter(name_icontains=query) | Student.objects.filter(course_icontains=query)
    else:
        students = Student.objects.all()

    paginator = Paginator(students, 5)
    page_number = request.GET.get('page')
    page_obj = paginator.get_page(page_number)

    return render(request, 'student_list.html', {
        'page_obj': page_obj,
        'request': request
    })


@login_required
def student_listuser(request):
    query = request.GET.get('q')
    if query:
        students = Student.objects.filter(name_icontains=query) | Student.objects.filter(course_icontains=query)
    else:
        students = Student.objects.all()

    paginator = Paginator(students, 5)
    page_number = request.GET.get('page')
    page_obj = paginator.get_page(page_number)

    return render(request, 'student_listuser.html', {
        'page_obj': page_obj,
        'request': request
    })


@login_required
def student_create(request):
    form = StudentForm(request.POST or None)
    if form.is_valid():
        student = form.save(commit=False)
        student.user = request.user
        student.save()
        if request.user.is_superuser:
            return redirect('student_list')
        else:
            return redirect('student_listuser')
    return render(request, 'student_form.html', {'form': form})


@login_required
def student_update(request, pk):
    student = Student.objects.get(id=pk)
    form = StudentForm(request.POST or None, instance=student)
    if form.is_valid():
        form.save()
        return redirect('student_list')
    return render(request, 'student_form.html', {'form': form})


@login_required
def student_delete(request, pk):
    student = Student.objects.get(id=pk)
    if request.method == 'POST':
        student.delete()
        return redirect('student_list')
    return render(request, 'student_confirm_delete.html', {'student': student})


def user_signup(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        email = request.POST.get('email')
        password1 = request.POST.get('password1')
        password2 = request.POST.get('password2')
        first_name = request.POST.get('first_name')
        last_name = request.POST.get('last_name')

        if password1 != password2:
            messages.error(request, "Passwords do not match.")
            return redirect('sign-up')
        if User.objects.filter(username=username).exists():
            messages.error(request, "Username already taken.")
            return redirect('sign-up')
        if User.objects.filter(email=email).exists():
            messages.error(request, "Email already registered.")
            return redirect('sign-up')

        user = User.objects.create_user(username=username, email=email, password=password1,
                                        first_name=first_name, last_name=last_name)
        user.save()
        messages.success(request, "Account created successfully! Please log in.")
        return redirect('login')
    return render(request,'signup.html')
