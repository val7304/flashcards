import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 10 },
        { duration: '1m', target: 30 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8081';

export default function () {

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    /* =========================
       CREATE CATEGORY
       ========================= */

    const categoryPayload = JSON.stringify({
        name: `cat-${Math.random()}`,
    });

    const resCat = http.post(
        `${BASE_URL}/api/categories`,
        categoryPayload,
        params
    );

    check(resCat, {
        'create category OK': (r) => r.status === 200 || r.status === 201,
    });

    if (!(resCat.status === 200 || resCat.status === 201)) {
        return; // stop iteration
    }

    let catId;
    try {
        catId = resCat.json('id');
    } catch (e) {
        console.log(`Category JSON parse error: ${e}`);
        return;
    }

    /* =========================
       CREATE FLASHCARD
       ========================= */

    const flashcardPayload = JSON.stringify({
        categoryId: catId,
        front: 'question test k6',
        back: 'réponse test k6',
    });

    // Optionnel mais recommandé sous charge
    sleep(0.05);

    const resFlash = http.post(
        `${BASE_URL}/api/flashcards`,
        flashcardPayload,
        params
    );

    check(resFlash, {
        'create flashcard OK': (r) => r.status === 200 || r.status === 201,
    });

    /* =========================
       READ OPERATIONS
       ========================= */

    const resGetCats = http.get(`${BASE_URL}/api/categories`);
    check(resGetCats, {
        'get categories OK': (r) => r.status === 200,
    });

    const resGetFlashcards = http.get(`${BASE_URL}/api/flashcards`);
    check(resGetFlashcards, {
        'get flashcards OK': (r) => r.status === 200,
    });

    sleep(1);
}
