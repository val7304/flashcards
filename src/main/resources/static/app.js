const API = '/api';

/* -------------------- Categories -------------------- */

let categoriesCache = {};

async function loadCategories() {
    const res = await loadCategories();
    const data = await res.json();

    categoriesCache = {};
    data.forEach(c => {
        categoriesCache[c.id] = c.name;
    });

    showCategories(data);
}

function loadCategories() {
    fetch(`${API}/categories`)
        .then(r => r.json())
        .then(showCategories);
}

function searchCategory() {
    const name = document.getElementById('searchCategory').value;
    fetch(`${API}/categories/search?name=${name}`)
        .then(r => r.json())
        .then(showCategories);
}

function createCategory() {
    const name = document.getElementById('categoryName').value;
    fetch(`${API}/categories`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name })
    }).then(loadCategories);
}

function updateCategory() {
    const id = document.getElementById('categoryId').value;
    const name = document.getElementById('categoryName').value;
    fetch(`${API}/categories/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name })
    }).then(loadCategories);
}

function deleteCategory() {
    const id = document.getElementById('deleteCategoryId').value;
    fetch(`${API}/categories/${id}`, { method: 'DELETE' })
        .then(loadCategories);
}

function showCategories(data) {
    const ul = document.getElementById('categories');
    ul.innerHTML = '';
    data.forEach(c => {
        const li = document.createElement('li');
        li.textContent = `${c.id} - ${c.name}`;
        ul.appendChild(li);
    });
}

/* -------------------- Flashcards -------------------- */

function loadFlashcards() {
    fetch(`${API}/flashcards`)
        .then(r => r.json())
        .then(showFlashcards);
}

function searchFlashcard() {
    const q = document.getElementById('searchFlashcard').value;
    fetch(`${API}/flashcards/search?question=${q}`)
        .then(r => r.json())
        .then(showFlashcards);
}

function createFlashcard() {
    const payload = getFlashcardPayload();
    fetch(`${API}/flashcards`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    }).then(loadFlashcards);
}

function updateFlashcard() {
    const id = document.getElementById('flashcardId').value;
    const payload = getFlashcardPayload();
    fetch(`${API}/flashcards/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    }).then(loadFlashcards);
}

function deleteFlashcard() {
    const id = document.getElementById('deleteFlashcardId').value;
    fetch(`${API}/flashcards/${id}`, { method: 'DELETE' })
        .then(loadFlashcards);
}

function getFlashcardPayload() {
    return {
        question: document.getElementById('flashcardQuestion').value,
        answer: document.getElementById('flashcardAnswer').value,
        categoryId: Number(document.getElementById('flashcardCategoryId').value)
    };
}

function showFlashcards(data) {
    const ul = document.getElementById('flashcards');
    ul.innerHTML = '';

    data.forEach(f => {
        const li = document.createElement('li');

        const meta = document.createElement('div');
        meta.innerHTML = `<strong>[${f.id}]</strong> — Category ID: <em>${f.categoryId}</em>`;
        meta.style.marginBottom = '4px';

        const q = document.createElement('div');
        q.innerHTML = `<strong>Q:</strong> ${f.question}`;
        q.style.cursor = 'pointer';

        const a = document.createElement('div');
        a.innerHTML = `<strong>A:</strong> ${f.answer}`;
        a.style.display = 'none';
        a.style.marginLeft = '1rem';

        q.onclick = () => {
            a.style.display = a.style.display === 'none' ? 'block' : 'none';
        };

        li.appendChild(meta);
        li.appendChild(q);
        li.appendChild(a);
        ul.appendChild(li);
    });
}


