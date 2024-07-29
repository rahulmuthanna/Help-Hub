/**
 * 
 */
// scripts.js
//home.js
    function createPost() {
            var username = '<%= session.getAttribute("username") %>';
            if (username) {
                window.location.href = 'createPost.jsp';
            } else {
                alert('You must log in to create a post');
            }
        }
//user details circle
document.addEventListener('DOMContentLoaded', (event) => {
    const showDataBtn = document.getElementById('showDataBtn');
    const problemStatementsDiv = document.getElementById('problemStatements');

    showDataBtn.addEventListener('click', () => {
        // Toggle the display of problem statements
        if (problemStatementsDiv.style.display === 'none' || problemStatementsDiv.style.display === '') {
            problemStatementsDiv.style.display = 'block';
            showDataBtn.textContent = 'Hide Data';
        } else {
            problemStatementsDiv.style.display = 'none';
            showDataBtn.textContent = 'Show Data';
        }
    });
});

//resize description of post
function autoResize(textarea) {
            textarea.style.height = 'auto'; // Reset height to auto to shrink as needed
            textarea.style.height = textarea.scrollHeight + 'px'; // Set height to scrollHeight
        }



// Function to handle the search input with autofill
function handleAutofill(inputElement, data) {
    inputElement.addEventListener('input', function() {
        let value = this.value.toLowerCase();
        let suggestions = data.filter(item => item.toLowerCase().includes(value));
        displaySuggestions(suggestions);
    });
}

function displaySuggestions(suggestions) {
    let suggestionsContainer = document.getElementById('suggestions');
    suggestionsContainer.innerHTML = ''; // Clear previous suggestions
    suggestions.forEach(suggestion => {
        let div = document.createElement('div');
        div.classList.add('suggestion-item');
        div.textContent = suggestion;
        div.addEventListener('click', function() {
            document.getElementById('searchInput').value = suggestion;
            suggestionsContainer.innerHTML = '';
        });
        suggestionsContainer.appendChild(div);
    });
}

// Function to validate the post creation form
function validatePostForm() {
    const problemStatement = document.getElementById('problemStatement').value.trim();
    const description = document.getElementById('description').value.trim();
    const keyword = document.getElementById('keyword').value.trim();

    if (!problemStatement || !description || !keyword) {
        alert('All fields are required. Please fill in all fields before submitting.');
        return false;
    }

    return true;
}

function submitForm() {
    if (validatePostForm()) {
        document.getElementById('postForm').submit();
    }
}


// Function to validate the login form
function validateLoginForm() {
    let username = document.getElementById('username').value.trim();
    let department = document.getElementById('department').value.trim();
    let contactNumber = document.getElementById('contactNumber').value.trim();

    if (!username || !department || !contactNumber) {
        alert('All fields are required.');
        return false;
    }

    if (!/^\d+$/.test(contactNumber)) {
        alert('Contact number must be numeric.');
        return false;
    }

    return true;
}

// Example of calling functions when the DOM is fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Example data for autofill
    const keywords = ['npx', 'JavaScript', 'React', 'Node.js', 'CSS', 'HTML'];
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        handleAutofill(searchInput, keywords);
    }

    // Add event listener for form submissions
    let postForm = document.getElementById('postForm');
    if (postForm) {
        postForm.addEventListener('submit', function(event) {
            if (!validatePostForm()) {
                event.preventDefault(); // Prevent form submission if validation fails
            }
        });
    }

    let loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            if (!validateLoginForm()) {
                event.preventDefault(); // Prevent form submission if validation fails
            }
        });
    }
});

        