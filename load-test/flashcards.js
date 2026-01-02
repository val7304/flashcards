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
    const payload = JSON.stringify({
        name: `cat-${Math.random()}`,
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    // 1. Créer catégorie
    const resCat = http.post(`${BASE_URL}/api/categories`, payload, params);
    console.log(`Cat status: ${resCat.status}`);
    console.log(`Cat body preview: ${resCat.body?.substring(0, 100)}`);

    check(resCat, {
        'cat OK': (r) => r.status === 200 || r.status === 201,
    });

    // 2. EXTRAIRE catId → Flashcard
    if ((resCat.status === 200 || resCat.status === 201) && resCat.body) {
        try {
            const responseJson = resCat.json();
            const catId = responseJson.id;  // ✅ DÉCLARÉ ICI

            console.log(`Tentative flashcard avec catId=${catId}`);

            const resFlash = http.post(`${BASE_URL}/api/flashcards`, {
                categoryId: catId,  // ✅ Maintenant valide
                front: "question test k6",
                back: "réponse test k6"
            }, params);

            console.log(`Flash status: ${resFlash.status}`);
            console.log(`Flash body preview: ${resFlash.body?.substring(0, 100)}`);

            check(resFlash, {
                'flashcard OK': (r) => r.status === 200 || r.status === 201,
            });
        } catch (e) {
            console.log(`JSON error: ${e}`);
        }
    } else {
        console.log(`Skip flashcard: cat status ${resCat.status}`);
    }

    // 3. GETs
    http.get(`${BASE_URL}/api/categories`);
    http.get(`${BASE_URL}/api/flashcards`);
    sleep(1);
}
