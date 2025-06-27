from django.db import models 
from django.contrib.auth.models import User

# Create your models here.
class Student(models.Model):
 name = models.CharField(max_length=100)
 email = models.EmailField(unique=True)
 age = models.PositiveIntegerField()
 course = models.CharField(max_length=100)
 created_at = models.DateTimeField(auto_now_add=True)
 user = models.ForeignKey(User, on_delete= models.CASCADE)

def str (self):
    return self.name