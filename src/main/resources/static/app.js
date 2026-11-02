const API_CATEGORIES = "/api/categories";
const API_FLASHCARDS = "/api/flashcards";

let categoriesCache = [];
let flashcardsCache = [];

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("categoryForm").addEventListener("submit", saveCategory);
    document.getElementById("flashcardForm").addEventListener("submit", createFlashcard);
    document.getElementById("searchForm").addEventListener("submit", (event) => {
        event.preventDefault();
        renderFlashcards();
    });

    document.getElementById("searchCategory").addEventListener("input", renderFlashcards);

    document.getElementById("showAllButton").addEventListener("click", () => {
        document.getElementById("searchFlashcard").value = "";
        document.getElementById("searchCategory").value = "";
        renderFlashcards();
    });

    loadCategories();
    loadFlashcards();
});

async function apiRequest(url, options = {}) {
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `HTTP error ${response.status}`);
    }

    if (response.status === 204) {
        return null;
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

function showMessage(message, type = "success") {
    const element = document.getElementById("message");
    element.textContent = message;
    element.className = `alert alert-${type}`;
    element.classList.remove("d-none");

    window.setTimeout(() => {
        element.classList.add("d-none");
    }, 4000);
}

function getCategoryName(categoryId) {
    const category = categoriesCache.find(item =>
        String(item.id) === String(categoryId)
    );
    return category ? category.name : "Uncategorized";
}

async function loadCategories() {
    const container = document.getElementById("categories");

    try {
        const data = await apiRequest(API_CATEGORIES);
        categoriesCache = Array.isArray(data) ? data : [];

        renderCategories();
        populateCategorySelect();
        populateCategorySuggestions();
        renderFlashcards();
    } catch (error) {
        container.innerHTML = `
            <div class="text-danger small">
                Unable to load categories: ${escapeHtml(error.message)}
            </div>`;
    }
}

function renderCategories() {
    const container = document.getElementById("categories");

    if (categoriesCache.length === 0) {
        container.innerHTML = '<div class="text-muted small">No categories yet.</div>';
        return;
    }

    container.innerHTML = categoriesCache.map(category => `
        <div class="list-group-item category-item">
        
        <button class="category-name btn btn-link p-0 text-start text-decoration-none"
                type="button"
                onclick="selectCategoryById(${Number(category.id)})">
                <i class="fa-regular fa-hand-point-up mr-1"></i>
             ${escapeHtml(category.name)}
        </button>

            <div class="category-actions">
                <button class="btn btn-sm btn-outline-secondary"
                        type="button"
                        title="Edit category"
                        onclick="editCategory(${Number(category.id)})">
                    <i class="fa-solid fa-pen"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger"
                        type="button"
                        title="Delete category"
                        onclick="deleteCategory(${Number(category.id)})">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </div>
        </div>
    `).join("");
}


function selectCategoryById(id) {
    const category = categoriesCache.find(
        item => String(item.id) === String(id)
    );

    if (!category) return;

    const searchCategory = document.getElementById("searchCategory");

    searchCategory.value = category.name;

    renderFlashcards();
}


function populateCategorySelect() {
    const select = document.getElementById("flashcardCategoryId");
    const currentValue = select.value;

    select.innerHTML = '<option value="">Select a category</option>';

    categoriesCache.forEach(category => {
        const option = document.createElement("option");
        option.value = category.id;
        option.textContent = category.name;
        select.appendChild(option);
    });

    if (categoriesCache.some(category => String(category.id) === currentValue)) {
        select.value = currentValue;
    }
}


function populateCategorySuggestions() {
    const datalist = document.getElementById("categorySuggestions");

    datalist.innerHTML = "";

    categoriesCache.forEach(category => {
        const option = document.createElement("option");

        option.value = category.name;

        datalist.appendChild(option);
    });
}


async function saveCategory(event) {
    event.preventDefault();

    const id = document.getElementById("categoryId").value;
    const name = document.getElementById("categoryName").value.trim();

    if (!name) {
        showMessage("Please enter a category name.", "warning");
        return;
    }

    const payload = { name };

    try {
        if (id) {
            await apiRequest(`${API_CATEGORIES}/${id}`, {
                method: "PUT",
                body: JSON.stringify(payload)
            });
            showMessage("Category updated successfully.");
        } else {
            await apiRequest(API_CATEGORIES, {
                method: "POST",
                body: JSON.stringify(payload)
            });
            showMessage("Category created successfully.");
        }

        resetCategoryForm();
        await loadCategories();
    } catch (error) {
        showMessage(`Unable to save category: ${error.message}`, "danger");
    }
}

function editCategory(id) {
    const category = categoriesCache.find(item => String(item.id) === String(id));
    if (!category) return;

    document.getElementById("categoryId").value = category.id;
    document.getElementById("categoryName").value = category.name;
    document.getElementById("categorySubmit").textContent = "Update";
    document.getElementById("cancelCategoryEdit").classList.remove("d-none");
    document.getElementById("categoryName").focus();
}

function resetCategoryForm() {
    document.getElementById("categoryForm").reset();
    document.getElementById("categoryId").value = "";
    document.getElementById("categorySubmit").textContent = "Create";
    document.getElementById("cancelCategoryEdit").classList.add("d-none");
}

async function deleteCategory(id) {
    const category = categoriesCache.find(item => String(item.id) === String(id));
    if (!category) return;

    if (!confirm(`Be careful: if you remove the category, all the cards included in it will also be deleted. Delete category "${category.name}"?`)) return;

    try {
        await apiRequest(`${API_CATEGORIES}/${id}`, { method: "DELETE" });
        showMessage("Category deleted successfully.");
        await loadCategories();
        await loadFlashcards();
    } catch (error) {
        showMessage(`Unable to delete category: ${error.message}`, "danger");
    }
}

async function createFlashcard(event) {
    event.preventDefault();

    const question = document.getElementById("flashcardQuestion").value.trim();
    const answer = document.getElementById("flashcardAnswer").value.trim();
    const categoryId = document.getElementById("flashcardCategoryId").value;

    if (!question || !answer || !categoryId) {
        showMessage("Please fill in all flashcard fields.", "warning");
        return;
    }

    const payload = {
        question,
        answer,
        categoryId: Number(categoryId)
    };

    try {
        await apiRequest(API_FLASHCARDS, {
            method: "POST",
            body: JSON.stringify(payload)
        });

        document.getElementById("flashcardForm").reset();
        showMessage("Flashcard created successfully.");
        await loadFlashcards();
    } catch (error) {
        showMessage(`Unable to create flashcard: ${error.message}`, "danger");
    }
}

async function loadFlashcards() {
    const container = document.getElementById("flashcards");

    try {
        const data = await apiRequest(API_FLASHCARDS);
        flashcardsCache = Array.isArray(data) ? data : [];
        renderFlashcards();
    } catch (error) {
        container.innerHTML = `
            <div class="text-danger">
                Unable to load flashcards: ${escapeHtml(error.message)}
            </div>`;
    }
}

function renderFlashcards() {
    const container = document.getElementById("flashcards");

    const questionTerm = document.getElementById("searchFlashcard")
        .value.trim().toLowerCase();

    const categoryTerm = document.getElementById("searchCategory")
        .value.trim().toLowerCase();

    const filtered = flashcardsCache.filter(card => {
        const question = String(card.question || "").toLowerCase();
        const categoryName = getCategoryName(card.categoryId).toLowerCase();

        const matchesQuestion =
            !questionTerm || question.includes(questionTerm);

        const matchesCategory =
            !categoryTerm || categoryName.includes(categoryTerm);

        return matchesQuestion && matchesCategory;
    });

    document.getElementById("flashcardCount").textContent = filtered.length;

    if (filtered.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="fa-regular fa-folder-open fa-2x mb-2"></i>
                <p class="mb-0">No flashcards found.</p>
            </div>`;
        return;
    }

    container.innerHTML = filtered.map(card => {
        const id = Number(card.id);

        return `
            <article class="flashcard-item" id="flashcard-${id}">

                <!-- QUESTION AND ACTIONS -->
                <div class="flashcard-top">

                    <div class="flashcard-header-row">

                        <div class="flashcard-title-area">

                            <p class="flashcard-question"
                               role="button"
                               tabindex="0"
                               onclick="toggleAnswer(${id})"
                               onkeydown="if(event.key==='Enter'||event.key===' '){
                                   event.preventDefault();
                                   toggleAnswer(${id});
                               }">

                                ${escapeHtml(card.question)}

                            </p>

                            <div class="flashcard-meta">
                            <i class="fa-solid fa-folder me-1"></i>
                                ${escapeHtml(getCategoryName(card.categoryId))}
                            </div>

                        </div>

                        <div class="flashcard-actions">

                            <button class="btn btn-sm btn-outline-primary"
                                    type="button"
                                    onclick="toggleEditor(${id})">

                                <i class="fa-solid fa-pen me-1"></i>
                                Edit

                            </button>

                            <button class="btn btn-sm btn-outline-danger"
                                    type="button"
                                    onclick="deleteFlashcard(${id})">

                                <i class="fa-solid fa-trash me-1"></i>
                                Delete

                            </button>

                        </div>

                    </div>

                </div>

                <!-- ANSWER AND COPY BUTTON -->
                <div class="flashcard-answer" id="answer-${id}">

                    <div class="flashcard-answer-text">
                        ${escapeHtml(card.answer)}
                    </div>

                    <button class="btn btn-sm btn-outline-secondary flashcard-copy"
                            type="button"
                            onclick="copyAnswer(${id})">

                        <i class="fa-regular fa-copy me-1"></i>
                        Copy answer

                    </button>

                </div>

                <!-- INLINE EDITOR -->
                <div class="flashcard-editor" id="editor-${id}">

                    <form onsubmit="updateFlashcard(event, ${id})">

                        <div class="mb-3">

                            <label class="form-label"
                                   for="edit-question-${id}">
                                Question
                            </label>

                            <textarea class="form-control"
                                      id="edit-question-${id}"
                                      rows="2"
                                      required>${escapeHtml(card.question)}</textarea>

                        </div>

                        <div class="mb-3">

                            <label class="form-label"
                                   for="edit-answer-${id}">
                                Answer
                            </label>

                            <textarea class="form-control"
                                      id="edit-answer-${id}"
                                      rows="3"
                                      required>${escapeHtml(card.answer)}</textarea>

                        </div>

                        <div class="mb-3">

                            <label class="form-label"
                                   for="edit-category-${id}">
                                Category
                            </label>

                            <select class="form-select"
                                    id="edit-category-${id}"
                                    required>

                                ${categoriesCache.map(category => `
                                    <option value="${category.id}"
                                        ${String(category.id) === String(card.categoryId)
                                            ? "selected" : ""}>
                                        ${escapeHtml(category.name)}
                                    </option>
                                `).join("")}

                            </select>

                        </div>

                        <button type="submit"
                                class="btn btn-primary btn-sm">
                            Save changes
                        </button>

                        <button type="button"
                                class="btn btn-outline-secondary btn-sm"
                                onclick="toggleEditor(${id})">
                            Cancel
                        </button>

                    </form>

                </div>

            </article>
        `;
    }).join("");
}

function toggleAnswer(id) {
    const answer = document.getElementById(`answer-${id}`);
    if (answer) answer.classList.toggle("visible");
}

function toggleEditor(id) {
    const editor = document.getElementById(`editor-${id}`);
    if (editor) editor.classList.toggle("visible");
}

async function copyAnswer(id) {
    const card = flashcardsCache.find(item => String(item.id) === String(id));
    if (!card) return;

    try {
        await navigator.clipboard.writeText(card.answer || "");
        showMessage("Answer copied to clipboard.");
    } catch (error) {
        showMessage("Unable to copy the answer. Check browser permissions.", "danger");
    }
}

async function updateFlashcard(event, id) {
    event.preventDefault();

    const question = document.getElementById(`edit-question-${id}`).value.trim();
    const answer = document.getElementById(`edit-answer-${id}`).value.trim();
    const categoryId = document.getElementById(`edit-category-${id}`).value;

    if (!question || !answer || !categoryId) {
        showMessage("Please fill in all flashcard fields.", "warning");
        return;
    }

    const payload = {
        question,
        answer,
        categoryId: Number(categoryId)
    };

    try {
        await apiRequest(`${API_FLASHCARDS}/${id}`, {
            method: "PUT",
            body: JSON.stringify(payload)
        });

        showMessage("Flashcard updated successfully.");
        await loadFlashcards();
    } catch (error) {
        showMessage(`Unable to update flashcard: ${error.message}`, "danger");
    }
}

async function deleteFlashcard(id) {
    if (!confirm("Delete this flashcard?")) return;

    try {
        await apiRequest(`${API_FLASHCARDS}/${id}`, { method: "DELETE" });
        showMessage("Flashcard deleted successfully.");
        await loadFlashcards();
    } catch (error) {
        showMessage(`Unable to delete flashcard: ${error.message}`, "danger");
    }
}

function escapeHtml(value) {
    return String(value ?? "").replace(/[&<>"']/g, character => {
        const entities = {
            "&": "&amp;",
            "<": "&lt;",
            ">": "&gt;",
            '"': "&quot;",
            "'": "&#039;"
        };
        return entities[character];
    });
}