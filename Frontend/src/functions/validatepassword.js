export const validatePassword = (password) => {
    let error = '';
    if (!password) {
      error = 'Password is required';
    }
    if (password.length < 6) {
      return error;
    }
    if (!/[A-Z]/.test(password)) {
      error = 'Password must contain at least one uppercase letter';
    } else if (!/[a-z]/.test(password)) {
      error = 'Password must contain at least one lowercase letter';
    } else if (!/[0-9]/.test(password)) {
      error = 'Password must contain at least one number';
    } else if (!/[!@#$%^&*]/.test(password)) {
      error = 'Password must contain at least one special character';
    } else if (password.length < 8) {
      error = 'Password must be at least 8 characters long';
    }
    return error;
  };