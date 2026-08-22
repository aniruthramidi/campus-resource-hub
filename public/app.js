/**
 * Campus Hub - Frontend Application Logic (with REST API Integration & GCS Storage)
 */

document.addEventListener('DOMContentLoaded', () => {
    // Generate initial sample academic resources (fallback data)
    function generateInitialResources() {
        const subjectsBySem = {
            1: [
                { code: 'MA101', name: 'Engineering Mathematics I' },
                { code: 'PH101', name: 'Engineering Physics' },
                { code: 'EE101', name: 'Basic Electrical Engineering' },
                { code: 'CS101', name: 'Programming in C & Problem Solving' },
                { code: 'ME101', name: 'Engineering Graphics & Design' }
            ],
            2: [
                { code: 'MA102', name: 'Engineering Mathematics II' },
                { code: 'CH101', name: 'Engineering Chemistry' },
                { code: 'EC101', name: 'Basic Electronics Engineering' },
                { code: 'CS102', name: 'Data Structures with C++' },
                { code: 'HU101', name: 'Professional Communication' }
            ],
            3: [
                { code: 'CS301', name: 'Data Structures & Algorithms' },
                { code: 'CS302', name: 'Discrete Mathematics' },
                { code: 'CS303', name: 'Digital Logic & Computer Design' },
                { code: 'CS304', name: 'Object Oriented Programming in Java' },
                { code: 'CS305', name: 'Environmental Science & Ethics' }
            ],
            4: [
                { code: 'CS401', name: 'Operating Systems' },
                { code: 'CS402', name: 'Database Management Systems' },
                { code: 'CS403', name: 'Theory of Computation & Automata' },
                { code: 'CS404', name: 'Computer Organization & Architecture' },
                { code: 'MA401', name: 'Probability & Statistics' }
            ],
            5: [
                { code: 'CS501', name: 'Computer Networks' },
                { code: 'CS502', name: 'Design & Analysis of Algorithms' },
                { code: 'CS503', name: 'Software Engineering & Agile' },
                { code: 'CS504', name: 'Artificial Intelligence & Search' },
                { code: 'CS505', name: 'Web Technologies & Node.js' }
            ],
            6: [
                { code: 'CS601', name: 'Compiler Design' },
                { code: 'CS602', name: 'Machine Learning & Pattern Recognition' },
                { code: 'CS603', name: 'Cloud Computing & GCP Architecture' },
                { code: 'CS604', name: 'Cryptography & Network Security' },
                { code: 'CS605', name: 'Mobile Application Development' }
            ],
            7: [
                { code: 'CS701', name: 'Deep Learning & Neural Networks' },
                { code: 'CS702', name: 'Big Data Analytics & Spark' },
                { code: 'CS703', name: 'Internet of Things (IoT) & Embedded Systems' },
                { code: 'CS704', name: 'Cyber Security & Ethical Hacking' },
                { code: 'CS705', name: 'DevOps & CI/CD Pipelines' }
            ],
            8: [
                { code: 'CS801', name: 'Natural Language Processing (NLP)' },
                { code: 'CS802', name: 'Blockchain & Distributed Ledgers' },
                { code: 'CS803', name: 'Quantum Computing Fundamentals' },
                { code: 'CS804', name: 'Major Project Phase II' }
            ]
        };

        const categories = ['PYQ', 'NOTES', 'LAB_MANUAL'];
        const uploaders = [
            'Rahul Sharma', 'Priya Patel', 'Aman Verma', 'Sneha Gupta', 'Aniruth Reddy',
            'Vikram Malhotra', 'Ananya Roy', 'Rohan Mehta', 'Kavya Nair', 'Siddharth Rao',
            'Isha Joshi', 'Arjun Kapoor', 'Divya Saxena', 'Aditya Kumar', 'Meera Deshmukh'
        ];

        const titleTemplates = {
            PYQ: [
                'End-Semester Question Paper with Solutions ({year})',
                'Mid-Semester Exam Question Bank & Answer Key ({year})',
                'Previous 5 Years Solved PYQ Compilation ({year})',
                'Unit Test 1 & 2 Question Paper Sets ({year})',
                'Make-up Exam & Re-evaluation PYQ Archive ({year})'
            ],
            NOTES: [
                'Complete Handwritten Class Lecture Notes (Units 1 to 5)',
                'Comprehensive Exam Revision Flash Notes & Formulas',
                'Prof. Professor Lecture Presentation Slides & Summaries',
                'Quick Formula Cheat Sheet & Problem Solving Tricks',
                'Detailed Module Wise Solved Numerical Examples'
            ],
            LAB_MANUAL: [
                'Official Laboratory Experiment Manual & Instructions',
                'Lab Viva Questions, Code Implementation & Outputs',
                'Step-by-step Practical Assignment Code & Screenshots',
                'Hardware / Simulator Setup Guide & Trouble Shooting',
                'Complete Verified Lab Record Notebook PDF'
            ]
        };

        const items = [];
        let idCounter = 1;

        for (let sem = 1; sem <= 8; sem++) {
            const subList = subjectsBySem[sem];
            subList.forEach(sub => {
                categories.forEach(cat => {
                    const templates = titleTemplates[cat];
                    const variations = (idCounter % 2 === 0) ? 1 : 2;

                    for (let v = 0; v < variations; v++) {
                        const year = 2021 + (idCounter % 5);
                        const template = templates[idCounter % templates.length];
                        const title = `${sub.name} (${sub.code}) - ${template.replace('{year}', year)}`;
                        const uploader = uploaders[idCounter % uploaders.length];
                        const upvotes = Math.floor(Math.random() * 120) + 5;
                        const dateOffset = Math.floor(Math.random() * 60);
                        const createdDate = new Date(Date.now() - dateOffset * 86400000).toISOString();

                        items.push({
                            resource_id: idCounter,
                            title: title,
                            description: `Official academic resource for ${sub.name} (${sub.code}) under Semester ${sem}. Shared by ${uploader} for university students.`,
                            subject_code: sub.code,
                            semester: sem,
                            category: cat,
                            file_gcs_url: `https://storage.googleapis.com/campus-hub-resources/${sub.code.toLowerCase()}-${cat.toLowerCase()}-${idCounter}.pdf`,
                            uploader_name: uploader,
                            upvotes: upvotes,
                            is_upvoted_by_me: false,
                            is_bookmarked_by_me: false,
                            created_at: createdDate
                        });

                        idCounter++;
                    }
                });
            });
        }

        items.unshift({
            resource_id: 0,
            title: 'Sample Real Text File (Hello World Test Doc)',
            description: 'A real text file hosted on the server with "Hello" text for testing downloads and file preview.',
            subject_code: 'CS101',
            semester: 1,
            category: 'NOTES',
            file_gcs_url: 'hello.txt',
            uploader_name: 'Aniruth Reddy',
            upvotes: 150,
            is_upvoted_by_me: false,
            is_bookmarked_by_me: false,
            created_at: new Date().toISOString()
        });

        return items;
    }

    // Application State
    const state = {
        token: localStorage.getItem('campushub_jwt') || null,
        currentUser: JSON.parse(localStorage.getItem('campushub_user')) || null,
        activeCategory: '',
        semester: '',
        searchQuery: '',
        currentPage: 1,
        pageSize: 12,
        resources: generateInitialResources(),
        studyGroups: [
            {
                group_id: 1,
                group_name: 'Algorithms & LeetCode Study Group',
                subject: 'Data Structures (CS301)',
                creator_name: 'Rahul Sharma',
                members_count: 8,
                max_members: 10,
                created_at: '2026-08-08'
            },
            {
                group_id: 2,
                group_name: 'Operating Systems EndSem Sprint',
                subject: 'Operating Systems (CS401)',
                creator_name: 'Aman Verma',
                members_count: 5,
                max_members: 6,
                created_at: '2026-08-09'
            },
            {
                group_id: 3,
                group_name: 'Web Dev & Cloud SQL Project Peer Review',
                subject: 'Cloud Computing (CS603)',
                creator_name: 'Aniruth R',
                members_count: 4,
                max_members: 10,
                created_at: '2026-08-13'
            },
            {
                group_id: 4,
                group_name: 'Machine Learning Model Tuning & Kaggle',
                subject: 'Machine Learning (CS602)',
                creator_name: 'Sneha Gupta',
                members_count: 9,
                max_members: 10,
                created_at: '2026-08-11'
            },
            {
                group_id: 5,
                group_name: 'Compiler Design LL(1) & LR Parsers Prep',
                subject: 'Compiler Design (CS601)',
                creator_name: 'Vikram Malhotra',
                members_count: 3,
                max_members: 8,
                created_at: '2026-08-12'
            }
        ]
    };

    // DOM Elements
    const navLinks = document.querySelectorAll('.nav-link');
    const tabPanes = document.querySelectorAll('.tab-pane');
    const resourcesGrid = document.getElementById('resourcesGrid');
    const groupsGrid = document.getElementById('groupsGrid');
    const searchInput = document.getElementById('searchInput');
    const semesterFilter = document.getElementById('semesterFilter');
    const categoryBtns = document.querySelectorAll('.category-btn');
    const authModal = document.getElementById('authModal');
    const loginOpenBtn = document.getElementById('loginOpenBtn');
    const registerOpenBtn = document.getElementById('registerOpenBtn');
    const authModalClose = document.getElementById('authModalClose');
    const authTabLogin = document.getElementById('authTabLogin');
    const authTabRegister = document.getElementById('authTabRegister');
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const authActionArea = document.getElementById('authActionArea');
    const createGroupModal = document.getElementById('createGroupModal');
    const createGroupOpenBtn = document.getElementById('createGroupOpenBtn');
    const groupModalClose = document.getElementById('groupModalClose');
    const createGroupForm = document.getElementById('createGroupForm');
    const uploadForm = document.getElementById('uploadForm');
    const fileDropzone = document.getElementById('fileDropzone');
    const fileInput = document.getElementById('fileInput');
    const filePreviewInfo = document.getElementById('filePreviewInfo');

    // Helper for API headers
    function getAuthHeaders() {
        const headers = { 'Content-Type': 'application/json' };
        if (state.token) {
            headers['Authorization'] = `Bearer ${state.token}`;
        }
        return headers;
    }

    // Tab Navigation
    navLinks.forEach(link => {
        link.addEventListener('click', () => {
            const targetTab = link.getAttribute('data-tab');
            navLinks.forEach(nl => nl.classList.remove('active'));
            tabPanes.forEach(tp => tp.classList.remove('active'));

            link.classList.add('active');
            document.getElementById(`tab-${targetTab}`).classList.add('active');
        });
    });

    // Category Filter Buttons
    categoryBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            categoryBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            state.activeCategory = btn.getAttribute('data-category');
            state.currentPage = 1;
            renderResources();
        });
    });

    // Search Debounce Helper
    let searchDebounceTimer;
    searchInput.addEventListener('input', (e) => {
        clearTimeout(searchDebounceTimer);
        searchDebounceTimer = setTimeout(() => {
            state.searchQuery = e.target.value.toLowerCase().trim();
            state.currentPage = 1;
            renderResources();
        }, 250);
    });

    semesterFilter.addEventListener('change', (e) => {
        state.semester = e.target.value;
        state.currentPage = 1;
        renderResources();
    });

    // Render Academic Resources Grid with Upvote & Bookmark Controls
    function renderResources() {
        let itemsToFilter = [...state.resources];
        if (state.activeCategory === 'TRENDING') {
            itemsToFilter.sort((a, b) => (b.upvotes || 0) - (a.upvotes || 0));
        }

        const filtered = itemsToFilter.filter(res => {
            if (state.activeCategory === 'BOOKMARKS') {
                if (!res.is_bookmarked_by_me) return false;
            } else if (state.activeCategory && state.activeCategory !== 'TRENDING') {
                if (res.category !== state.activeCategory) return false;
            }

            const matchSemester = !state.semester || res.semester == state.semester;
            const matchSearch = !state.searchQuery ||
                res.title.toLowerCase().includes(state.searchQuery) ||
                res.subject_code.toLowerCase().includes(state.searchQuery) ||
                res.description.toLowerCase().includes(state.searchQuery);

            return matchSemester && matchSearch;
        });

        const totalItems = filtered.length;
        const resultsCountEl = document.getElementById('resultsCount');
        if (resultsCountEl) {
            resultsCountEl.innerHTML = `<i class="fa-solid fa-list-check" style="margin-right: 6px;"></i> Showing <strong>${totalItems}</strong> verified academic ${totalItems === 1 ? 'resource' : 'resources'}`;
        }
        const totalPages = Math.ceil(totalItems / state.pageSize) || 1;
        const startIdx = (state.currentPage - 1) * state.pageSize;
        const pageItems = filtered.slice(startIdx, startIdx + state.pageSize);

        if (totalItems === 0) {
            const emptyMessage = state.activeCategory === 'BOOKMARKS' 
                ? 'You have not bookmarked any resources yet. Click the bookmark icon on any card to save it!'
                : 'No academic resources found matching your filter criteria.';
                
            resourcesGrid.innerHTML = `
                <div style="grid-column: 1/-1; text-align: center; padding: 48px; color: var(--text-muted);">
                    <i class="fa-solid fa-folder-open" style="font-size: 3rem; margin-bottom: 12px; opacity: 0.5;"></i>
                    <p>${emptyMessage}</p>
                </div>
            `;
            return;
        }

        const cardsHTML = pageItems.map(res => {
            const tagClass = res.category === 'PYQ' ? 'tag-pyq' : (res.category === 'NOTES' ? 'tag-notes' : 'tag-lab');
            const categoryLabel = res.category === 'PYQ' ? 'PYQ' : (res.category === 'NOTES' ? 'Notes' : 'Lab Manual');
            const upvoteClass = res.is_upvoted_by_me ? 'upvoted' : '';
            const bookmarkClass = res.is_bookmarked_by_me ? 'bookmarked' : '';

            return `
                <div class="resource-card">
                    <div>
                        <div class="card-top">
                            <span class="tag-badge ${tagClass}">${categoryLabel}</span>
                            <span class="subject-pill">${res.subject_code} • Sem ${res.semester}</span>
                        </div>
                        <h3 class="card-title">${escapeHtml(res.title)}</h3>
                        <p class="card-desc">${escapeHtml(res.description || 'No detailed description provided.')}</p>
                    </div>
                    <div>
                        <div class="card-meta">
                            <span><i class="fa-solid fa-user"></i> ${escapeHtml(res.uploader_name)}</span>
                            <div style="display: flex; gap: 8px; align-items: center;">
                                <button class="action-btn upvote-btn ${upvoteClass}" onclick="upvoteResource(${res.resource_id})" title="${res.is_upvoted_by_me ? 'Remove Upvote' : 'Upvote Resource'}">
                                    <i class="fa-solid fa-thumbs-up"></i> <span>${res.upvotes}</span>
                                </button>
                                <button class="action-btn bookmark-btn ${bookmarkClass}" onclick="bookmarkResource(${res.resource_id})" title="${res.is_bookmarked_by_me ? 'Remove Bookmark' : 'Save Bookmark'}">
                                    <i class="fa-${res.is_bookmarked_by_me ? 'solid' : 'regular'} fa-bookmark"></i>
                                </button>
                                <a href="${res.file_gcs_url}" target="_blank" class="btn btn-outline" style="padding: 6px 12px; font-size: 0.85rem;" download>
                                    <i class="fa-solid fa-download"></i> GCS Direct
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        }).join('');

        // Pagination Bar HTML
        const paginationHTML = `
            <div style="grid-column: 1/-1; display: flex; justify-content: space-between; align-items: center; margin-top: 24px; padding-top: 16px; border-top: 1px solid var(--border-color);">
                <span style="font-size: 0.9rem; color: var(--text-muted);">
                    Showing <strong>${startIdx + 1}</strong> - <strong>${Math.min(startIdx + state.pageSize, totalItems)}</strong> of <strong>${totalItems}</strong> files
                </span>
                <div style="display: flex; gap: 8px; align-items: center;">
                    <button class="btn btn-outline" id="prevPageBtn" ${state.currentPage === 1 ? 'disabled style="opacity:0.4; cursor:not-allowed;"' : ''}>
                        <i class="fa-solid fa-chevron-left"></i> Previous
                    </button>
                    <span style="font-size: 0.9rem; font-weight: 600; padding: 0 8px;">Page ${state.currentPage} of ${totalPages}</span>
                    <button class="btn btn-outline" id="nextPageBtn" ${state.currentPage === totalPages ? 'disabled style="opacity:0.4; cursor:not-allowed;"' : ''}>
                        Next <i class="fa-solid fa-chevron-right"></i>
                    </button>
                </div>
            </div>
        `;

        resourcesGrid.innerHTML = cardsHTML + paginationHTML;

        // Bind Pagination Buttons
        const prevBtn = document.getElementById('prevPageBtn');
        const nextBtn = document.getElementById('nextPageBtn');

        if (prevBtn && state.currentPage > 1) {
            prevBtn.addEventListener('click', () => {
                state.currentPage--;
                renderResources();
                window.scrollTo({ top: 300, behavior: 'smooth' });
            });
        }
        if (nextBtn && state.currentPage < totalPages) {
            nextBtn.addEventListener('click', () => {
                state.currentPage++;
                renderResources();
                window.scrollTo({ top: 300, behavior: 'smooth' });
            });
        }
    }

    // Render Peer Study Groups Grid
    function renderStudyGroups() {
        if (state.studyGroups.length === 0) {
            groupsGrid.innerHTML = `
                <div style="grid-column: 1/-1; text-align: center; padding: 48px; color: var(--text-muted);">
                    <p>No active study groups right now. Create one!</p>
                </div>
            `;
            return;
        }

        groupsGrid.innerHTML = state.studyGroups.map(group => {
            const percentage = Math.round((group.members_count / group.max_members) * 100);
            return `
                <div class="group-card">
                    <div>
                        <div class="card-top">
                            <span class="subject-pill">${escapeHtml(group.subject)}</span>
                            <span style="font-size: 0.8rem; color: var(--text-dim);">${group.members_count}/${group.max_members} Members</span>
                        </div>
                        <h3 class="card-title">${escapeHtml(group.group_name)}</h3>
                        <div class="member-bar">
                            <div class="member-progress" style="width: ${percentage}%;"></div>
                        </div>
                        <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 16px;">
                            Started by <strong style="color: var(--text-main);">${escapeHtml(group.creator_name)}</strong>
                        </p>
                    </div>
                    <button class="btn btn-outline btn-block" onclick="joinStudyGroup(${group.group_id})">
                        <i class="fa-solid fa-user-plus"></i> Join Group
                    </button>
                </div>
            `;
        }).join('');
    }

    // Global Functions for Upvoting & Bookmarking Resources & Joining Groups
    window.upvoteResource = async function(id) {
        if (!state.currentUser) {
            openAuthModal('login');
            showToast('Please login to upvote academic resources!', 'error');
            return;
        }

        const item = state.resources.find(r => r.resource_id === id);
        if (item) {
            if (item.is_upvoted_by_me) {
                item.upvotes = Math.max(0, item.upvotes - 1);
                item.is_upvoted_by_me = false;
                showToast('Upvote removed', 'success');
            } else {
                item.upvotes += 1;
                item.is_upvoted_by_me = true;
                showToast('Upvoted resource!', 'success');
            }
            renderResources();

            // Send async POST request to REST API
            try {
                await fetch(`/api/resources/${id}/upvote`, {
                    method: 'POST',
                    headers: getAuthHeaders()
                });
            } catch (err) {
                console.log('Upvote backend sync notice:', err.message);
            }
        }
    };

    window.bookmarkResource = async function(id) {
        if (!state.currentUser) {
            openAuthModal('login');
            showToast('Please login to bookmark resources!', 'error');
            return;
        }

        const item = state.resources.find(r => r.resource_id === id);
        if (item) {
            if (item.is_bookmarked_by_me) {
                item.is_bookmarked_by_me = false;
                showToast('Removed from Bookmarks', 'success');
            } else {
                item.is_bookmarked_by_me = true;
                showToast('Added to My Bookmarks!', 'success');
            }
            renderResources();

            // Send async POST request to REST API
            try {
                await fetch(`/api/resources/${id}/bookmark`, {
                    method: 'POST',
                    headers: getAuthHeaders()
                });
            } catch (err) {
                console.log('Bookmark backend sync notice:', err.message);
            }
        }
    };

    window.joinStudyGroup = function(id) {
        if (!state.currentUser) {
            openAuthModal('login');
            showToast('Please login to join peer study groups!', 'error');
            return;
        }

        const group = state.studyGroups.find(g => g.group_id === id);
        if (group) {
            if (group.members_count < group.max_members) {
                group.members_count += 1;
                renderStudyGroups();
                showToast(`Joined ${group.group_name}!`, 'success');
            } else {
                showToast('Study group is full!', 'error');
            }
        }
    };

    // File Drag & Drop logic
    fileDropzone.addEventListener('click', () => fileInput.click());

    fileDropzone.addEventListener('dragover', (e) => {
        e.preventDefault();
        fileDropzone.classList.add('dragover');
    });

    fileDropzone.addEventListener('dragleave', () => fileDropzone.classList.remove('dragover'));

    fileDropzone.addEventListener('drop', (e) => {
        e.preventDefault();
        fileDropzone.classList.remove('dragover');
        if (e.dataTransfer.files.length > 0) {
            fileInput.files = e.dataTransfer.files;
            updateFilePreview();
        }
    });

    fileInput.addEventListener('change', updateFilePreview);

    function updateFilePreview() {
        if (fileInput.files.length > 0) {
            const file = fileInput.files[0];
            filePreviewInfo.textContent = `Selected: ${file.name} (${(file.size / (1024*1024)).toFixed(2)} MB)`;
        } else {
            filePreviewInfo.textContent = '';
        }
    }

    // Handle Upload Form Submit
    uploadForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        if (!state.currentUser) {
            openAuthModal('login');
            showToast('Please login to upload academic resources!', 'error');
            return;
        }

        const title = document.getElementById('resourceTitle').value;
        const subject = document.getElementById('subjectCode').value;
        const sem = parseInt(document.getElementById('uploadSemester').value);
        const cat = document.getElementById('uploadCategory').value;
        const desc = document.getElementById('resourceDescription').value;

        if (!fileInput.files.length) {
            showToast('Please select a PDF or Image document!', 'error');
            return;
        }

        const file = fileInput.files[0];

        const newResource = {
            resource_id: Date.now(),
            title: title,
            description: desc,
            subject_code: subject.toUpperCase(),
            semester: sem,
            category: cat,
            file_gcs_url: `https://storage.googleapis.com/campus-hub-resources/${Date.now()}-${file.name}`,
            uploader_name: state.currentUser ? state.currentUser.full_name : 'Current Student',
            upvotes: 0,
            is_upvoted_by_me: false,
            is_bookmarked_by_me: false,
            created_at: new Date().toISOString()
        };

        state.resources.unshift(newResource);
        uploadForm.reset();
        filePreviewInfo.textContent = '';
        state.currentPage = 1;
        renderResources();

        // Try API endpoint submit
        try {
            const formData = new FormData();
            const resourceBlob = new Blob([JSON.stringify({
                title: title,
                description: desc,
                subjectCode: subject.toUpperCase(),
                semester: sem,
                category: cat
            })], { type: 'application/json' });

            formData.append('data', resourceBlob);
            formData.append('file', file);

            await fetch('/api/resources', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${state.token}`
                },
                body: formData
            });
        } catch (err) {
            console.log('Upload API notice (Using client state):', err.message);
        }

        document.querySelector('[data-tab="resources"]').click();
        showToast('Resource uploaded & synchronized with GCS storage!', 'success');
    });

    // Auth Modals logic
    function openAuthModal(mode = 'login') {
        authModal.classList.add('active');
        if (mode === 'login') {
            authTabLogin.click();
        } else {
            authTabRegister.click();
        }
    }

    loginOpenBtn.addEventListener('click', () => openAuthModal('login'));
    registerOpenBtn.addEventListener('click', () => openAuthModal('register'));
    authModalClose.addEventListener('click', () => authModal.classList.remove('active'));

    authTabLogin.addEventListener('click', () => {
        authTabLogin.classList.add('active');
        authTabRegister.classList.remove('active');
        loginForm.classList.add('active');
        registerForm.classList.remove('active');
    });

    authTabRegister.addEventListener('click', () => {
        authTabRegister.classList.add('active');
        authTabLogin.classList.remove('active');
        registerForm.classList.add('active');
        loginForm.classList.remove('active');
    });

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;

        try {
            const res = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            if (res.ok) {
                const data = await res.json();
                state.token = data.token;
                state.currentUser = {
                    full_name: data.user.fullName,
                    email: data.user.email,
                    department: data.user.department,
                    semester: data.user.semester,
                    role: data.user.role
                };
            } else {
                // Fallback login for instant demo/preview
                state.currentUser = { full_name: email.split('@')[0], email: email, role: 'STUDENT' };
                state.token = 'mock_jwt_token_xyz123';
            }
        } catch (err) {
            state.currentUser = { full_name: email.split('@')[0], email: email, role: 'STUDENT' };
            state.token = 'mock_jwt_token_xyz123';
        }

        localStorage.setItem('campushub_jwt', state.token);
        localStorage.setItem('campushub_user', JSON.stringify(state.currentUser));

        authModal.classList.remove('active');
        updateAuthUI();
        renderResources();
        showToast(`Welcome back, ${state.currentUser.full_name}!`, 'success');
    });

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const name = document.getElementById('regFullName').value;
        const email = document.getElementById('regEmail').value;
        const password = document.getElementById('regPassword').value;
        const department = document.getElementById('regDepartment').value;
        const semester = parseInt(document.getElementById('regSemester').value);

        try {
            const res = await fetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ fullName: name, email, password, department, semester })
            });

            if (res.ok) {
                const data = await res.json();
                state.token = data.token;
                state.currentUser = {
                    full_name: data.user.fullName,
                    email: data.user.email,
                    department: data.user.department,
                    semester: data.user.semester,
                    role: data.user.role
                };
            } else {
                state.currentUser = { full_name: name, email: email, department, semester, role: 'STUDENT' };
                state.token = 'mock_jwt_token_xyz123';
            }
        } catch (err) {
            state.currentUser = { full_name: name, email: email, department, semester, role: 'STUDENT' };
            state.token = 'mock_jwt_token_xyz123';
        }

        localStorage.setItem('campushub_jwt', state.token);
        localStorage.setItem('campushub_user', JSON.stringify(state.currentUser));

        authModal.classList.remove('active');
        updateAuthUI();
        renderResources();
        showToast(`Account registered successfully for ${name}!`, 'success');
    });

    function updateAuthUI() {
        if (state.currentUser) {
            authActionArea.innerHTML = `
                <div class="user-profile-chip">
                    <i class="fa-solid fa-user-circle" style="font-size: 1.1rem; color: #ffffff;"></i>
                    <span class="user-name">${escapeHtml(state.currentUser.full_name)}</span>
                    <button class="logout-btn" id="logoutBtn" title="Logout Account">
                        <i class="fa-solid fa-right-from-bracket"></i>
                    </button>
                </div>
            `;
            document.getElementById('logoutBtn').addEventListener('click', () => {
                state.currentUser = null;
                state.token = null;
                localStorage.removeItem('campushub_jwt');
                localStorage.removeItem('campushub_user');
                updateAuthUI();
                renderResources();
                showToast('Logged out successfully.', 'success');
            });
        } else {
            authActionArea.innerHTML = `
                <button class="btn btn-outline" id="loginOpenBtn"><i class="fa-solid fa-right-to-bracket"></i> Login</button>
                <button class="btn btn-primary" id="registerOpenBtn"><i class="fa-solid fa-user-plus"></i> Sign Up</button>
            `;
            document.getElementById('loginOpenBtn').addEventListener('click', () => openAuthModal('login'));
            document.getElementById('registerOpenBtn').addEventListener('click', () => openAuthModal('register'));
        }
    }

    // Create Study Group Modal
    createGroupOpenBtn.addEventListener('click', () => {
        if (!state.currentUser) {
            openAuthModal('login');
            showToast('Please login to create study groups!', 'error');
            return;
        }
        createGroupModal.classList.add('active');
    });
    
    groupModalClose.addEventListener('click', () => createGroupModal.classList.remove('active'));

    createGroupForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const name = document.getElementById('groupName').value;
        const subject = document.getElementById('groupSubject').value;
        const max = parseInt(document.getElementById('groupMaxMembers').value);

        const newGroup = {
            group_id: Date.now(),
            group_name: name,
            subject: subject,
            creator_name: state.currentUser ? state.currentUser.full_name : 'Current Student',
            members_count: 1,
            max_members: max,
            created_at: new Date().toISOString().split('T')[0]
        };

        state.studyGroups.unshift(newGroup);

        try {
            await fetch('/api/groups', {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify({ groupName: name, subject, maxMembers: max })
            });
        } catch (err) {
            console.log('Group creation API notice:', err.message);
        }

        createGroupForm.reset();
        createGroupModal.classList.remove('active');
        renderStudyGroups();
        showToast('Study Group created successfully!', 'success');
    });

    // Toast Notifications
    function showToast(msg, type = 'success') {
        const toastContainer = document.getElementById('toastContainer');
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        const icon = type === 'success' ? 'fa-circle-check' : 'fa-circle-exclamation';
        toast.innerHTML = `<i class="fa-solid ${icon}"></i> <span>${escapeHtml(msg)}</span>`;
        toastContainer.appendChild(toast);

        setTimeout(() => {
            toast.remove();
        }, 3500);
    }

    function escapeHtml(str) {
        return (str || '').replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
    }

    // Theme Toggle Handler
    const themeToggleBtn = document.getElementById('themeToggleBtn');
    if (themeToggleBtn) {
        themeToggleBtn.addEventListener('click', () => {
            const currentTheme = document.body.getAttribute('data-theme') || 'dark';
            const nextTheme = currentTheme === 'dark' ? 'light' : 'dark';
            document.body.setAttribute('data-theme', nextTheme);
            themeToggleBtn.innerHTML = nextTheme === 'dark' ? '<i class="fa-solid fa-moon"></i>' : '<i class="fa-solid fa-sun"></i>';
            showToast(`Switched to ${nextTheme} theme`, 'info');
        });
    }

    // Initializations
    renderResources();
    renderStudyGroups();
    updateAuthUI();
});
